package jmu.lsk.wemedia.service.impl;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jmu.lsk.apis.article.IArticleClient;
import jmu.lsk.apis.schedule.IScheduleClient;
import jmu.lsk.common.constants.WemediaConstants;
import jmu.lsk.common.constants.WmNewsMessageConstants;
import jmu.lsk.common.exception.CustomException;
import jmu.lsk.model.article.dtos.ArticleDto;
import jmu.lsk.model.common.dtos.PageResponseResult;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.enums.AppHttpCodeEnum;
import jmu.lsk.model.common.enums.TaskTypeEnum;
import jmu.lsk.model.schedule.dtos.Task;
import jmu.lsk.model.wemedia.dtos.NewsAuthDto;
import jmu.lsk.model.wemedia.dtos.WmNewsDto;
import jmu.lsk.model.wemedia.dtos.WmNewsPageReqDto;
import jmu.lsk.model.wemedia.pojos.*;
import jmu.lsk.utils.common.ProtostuffUtil;
import jmu.lsk.utils.thread.WmThreadLocalUtil;
import jmu.lsk.wemedia.mapper.*;
import jmu.lsk.wemedia.service.WmNewsAutoScanService;
import jmu.lsk.wemedia.service.WmNewsService;
import jmu.lsk.wemedia.service.WmNewsTaskService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class WmNewsServiceImpl  extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {
 
    /**
     * 查询文章
     * @param dto
     * @return
     */
    @Override
    public ResponseResult findAll(WmNewsPageReqDto dto) {
 
        //1.检查参数
        if(dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //分页参数检查
        dto.checkParam();
        //获取当前登录人的信息
        WmUser user = WmThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
 
        //2.分页条件查询
        IPage page = new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<WmNews> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //状态精确查询
        if(dto.getStatus() != null){
            lambdaQueryWrapper.eq(WmNews::getStatus,dto.getStatus());
        }
 
        //频道精确查询
        if(dto.getChannelId() != null){
            lambdaQueryWrapper.eq(WmNews::getChannelId,dto.getChannelId());
        }
 
        //时间范围查询
        if(dto.getBeginPubDate()!=null && dto.getEndPubDate()!=null){
            lambdaQueryWrapper.between(WmNews::getPublishTime,dto.getBeginPubDate(),dto.getEndPubDate());
        }
 
        //关键字模糊查询
        if(StringUtils.isNotBlank(dto.getKeyword())){
            lambdaQueryWrapper.like(WmNews::getTitle,dto.getKeyword());
        }
 
        //查询当前登录用户的文章
        lambdaQueryWrapper.eq(WmNews::getUserId,user.getId());
 
        //发布时间倒序查询
        lambdaQueryWrapper.orderByDesc(WmNews::getCreatedTime);
 
        page = page(page,lambdaQueryWrapper);
 
        //3.结果返回
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),(int)page.getTotal());
        responseResult.setData(page.getRecords());
 
        return responseResult;
    }

    /**
     * 发布修改文章或保存为草稿
     * @param dto
     * @return
     */
    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;

    @Autowired
    private WmNewsTaskService wmNewsTaskService;

    @Override
    public ResponseResult submitNews(WmNewsDto dto){

        //0.条件判断
        if(dto == null || dto.getContent() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //1.保存或修改文章

        WmNews wmNews = new WmNews();
        //属性拷贝 属性名词和类型相同才能拷贝
        BeanUtils.copyProperties(dto,wmNews);
        //封面图片  list---> string
        if(dto.getImages() != null && dto.getImages().size() > 0){
            //[1dddfsd.jpg,sdlfjldk.jpg]-->   1dddfsd.jpg,sdlfjldk.jpg
            String imageStr = StringUtils.join(dto.getImages(), ",");
            wmNews.setImages(imageStr);
        }
        //如果当前封面类型为自动 -1
        if(dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)){
            wmNews.setType(null);
        }

        saveOrUpdateWmNews(wmNews);

        //2.判断是否为草稿  如果为草稿结束当前方法
        if(dto.getStatus().equals(WmNews.Status.NORMAL.getCode())){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }

        //3.不是草稿，保存文章内容图片与素材的关系
        //获取到文章内容中的图片信息
        List<String> materials =  ectractUrlInfo(dto.getContent());
        saveRelativeInfoForContent(materials,wmNews.getId());

        //4.不是草稿，保存文章封面图片与素材的关系，如果当前布局是自动，需要匹配封面图片
        saveRelativeInfoForCover(dto,wmNews,materials);

        //审核文章
//        wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        wmNewsTaskService.addNewsToTask(wmNews.getId(),wmNews.getPublishTime());

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);

    }

    /**
     * 第一个功能：如果当前封面类型为自动，则设置封面类型的数据
     * 匹配规则：
     * 1，如果内容图片大于等于1，小于3  单图  type 1
     * 2，如果内容图片大于等于3  多图  type 3
     * 3，如果内容没有图片，无图  type 0
     *
     * 第二个功能：保存封面图片与素材的关系
     * @param dto
     * @param wmNews
     * @param materials
     */
    private void saveRelativeInfoForCover(WmNewsDto dto, WmNews wmNews, List<String> materials) {

        List<String> images = dto.getImages();

        //如果当前封面类型为自动，则设置封面类型的数据
        if(dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)){
            //多图
            if(materials.size() >= 3){
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                images = materials.stream().limit(3).collect(Collectors.toList());
            }else if(materials.size() >= 1 && materials.size() < 3){
                //单图
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                images = materials.stream().limit(1).collect(Collectors.toList());
            }else {
                //无图
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            }

            //修改文章
            if(images != null && images.size() > 0){
                wmNews.setImages(StringUtils.join(images,","));
            }
            updateById(wmNews);
        }
        if(images != null && images.size() > 0){
            saveRelativeInfo(images,wmNews.getId(),WemediaConstants.WM_COVER_REFERENCE);
        }

    }


    /**
     * 处理文章内容图片与素材的关系
     * @param materials
     * @param newsId
     */
    private void saveRelativeInfoForContent(List<String> materials, Integer newsId) {
        saveRelativeInfo(materials,newsId,WemediaConstants.WM_CONTENT_REFERENCE);
    }

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    /**
     * 保存文章图片与素材的关系到数据库中
     * @param materials
     * @param newsId
     * @param type
     */
    private void saveRelativeInfo(List<String> materials, Integer newsId, Short type) {
        if(materials!=null && !materials.isEmpty()){
            //通过图片的url查询素材的id
            List<WmMaterial> dbMaterials = wmMaterialMapper.selectList(Wrappers.<WmMaterial>lambdaQuery().in(WmMaterial::getUrl, materials));

            //判断素材是否有效
            if(dbMaterials==null || dbMaterials.size() == 0){
                //手动抛出异常   第一个功能：能够提示调用者素材失效了，第二个功能，进行数据的回滚
                throw new CustomException(AppHttpCodeEnum.MATERIASL_REFERENCE_FAIL);
            }

            if(materials.size() != dbMaterials.size()){
                throw new CustomException(AppHttpCodeEnum.MATERIASL_REFERENCE_FAIL);
            }

            List<Integer> idList = dbMaterials.stream().map(WmMaterial::getId).collect(Collectors.toList());

            //批量保存
            wmNewsMaterialMapper.saveRelations(idList,newsId,type);
        }

    }


    /**
     * 提取文章内容中的图片信息
     * @param content
     * @return
     */
    private List<String> ectractUrlInfo(String content) {
        List<String> materials = new ArrayList<>();

        List<Map> maps = JSON.parseArray(content, Map.class);
        for (Map map : maps) {
            if(map.get("type").equals("image")){
                String imgUrl = (String) map.get("value");
                materials.add(imgUrl);
            }
        }

        return materials;
    }

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    /**
     * 保存或修改文章
     * @param wmNews
     */
    private void saveOrUpdateWmNews(WmNews wmNews) {
        //补全属性
        wmNews.setUserId(WmThreadLocalUtil.getUser().getId());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        wmNews.setEnable((short)1);//默认上架

        if(wmNews.getId() == null){
            //保存
            save(wmNews);
        }else {
            //修改
            //删除文章图片与素材的关系
            wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId,wmNews.getId()));
            updateById(wmNews);
        }
    }

    @Autowired
    private   IScheduleClient scheduleClient;

    /**
     * 消费延迟队列数据
     */
    @Scheduled(fixedRate = 1000)
    @Override
    @SneakyThrows
    public void scanNewsByTask() {


        ResponseResult responseResult = scheduleClient.poll(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType(), TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        if(responseResult.getCode().equals(200) && responseResult.getData() != null){
            String json_str = JSON.toJSONString(responseResult.getData());
            Task task = JSON.parseObject(json_str, Task.class);
            byte[] parameters = task.getParameters();
            WmNews wmNews = ProtostuffUtil.deserialize(parameters, WmNews.class);
            System.out.println(wmNews.getId()+"-----------");
            wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        }
    }

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public ResponseResult downOrUp(WmNewsDto dto) {
        if(dto.getId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        WmNews wmNews = getById(dto.getId());
        if(wmNews==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"文章不存在");
        }

        if(!wmNews.getStatus().equals(WmNews.Status.PUBLISHED.getCode())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"当前文章不是发布状态，不能上下架");
        }

        if(dto.getEnable()!=null&&dto.getEnable()>-1&&dto.getEnable()<2){
            update(Wrappers.<WmNews>lambdaUpdate().set(WmNews::getEnable,dto.getEnable())
                    .eq(WmNews::getId,dto.getId()));
        }

        //发送消息，通知article端修改文章配置
        if(wmNews.getArticleId() != null){
            Map<String,Object> map = new HashMap<>();
            map.put("articleId",wmNews.getArticleId());
            map.put("enable",dto.getEnable());
            kafkaTemplate.send(WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN_TOPIC,JSON.toJSONString(map));
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult collect(Integer id) {
        if(id==null)
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        WmNews wmNews = getById(id);
        return ResponseResult.okResult(wmNews);
    }

    @Override
    public ResponseResult deleteNews(Integer id) {
        if(id==null)
            return ResponseResult.errorResult(501,"文章Id不可缺少");
        WmNews wmNews = getById(id);

        if(wmNews==null){
            return ResponseResult.errorResult(1002,"文章不存在");
        }
        if(wmNews.getStatus()==9){
            return ResponseResult.errorResult(501,"文章已发布，不能删除");
        }
        removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult findList(NewsAuthDto dto) {
        dto.checkParam();
        IPage page = new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<WmNews> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(dto.getStatus()==null){
            lambdaQueryWrapper.select();
        }else{
            lambdaQueryWrapper.eq(WmNews::getStatus,dto.getStatus());
        }
        lambdaQueryWrapper.like(WmNews::getTitle,dto.getTitle());
        page = page(page,lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    @Override
    public ResponseResult findWmNewsVo(Integer id) {
        WmNews wmNews = getById(id);
        return ResponseResult.okResult(wmNews);
    }

    @Autowired
    private WmNewsMapper wmNewsMapper;



    @Override
    public ResponseResult updateStatus(Short status, NewsAuthDto dto) {
        dto.checkParam();
        WmNews wmNews = wmNewsMapper.selectOne(Wrappers.<WmNews>lambdaQuery().eq(WmNews::getId,dto.getId()));
        if(status.equals(WemediaConstants.WM_NEWS_AUTH_PASS)){
            ResponseResult responseResult = saveAppArticle(wmNews);
            wmNews.setStatus(WemediaConstants.WM_NEWS_AUTH_PASS);
            wmNews.setReason("人工审核成功");
            wmNewsMapper.updateById(wmNews);
            wmNews.setArticleId((Long) responseResult.getData());

            if(!responseResult.getCode().equals(200)){
                throw new RuntimeException("WmNewsAutoScanServiceImpl-文章审核，保存app端相关文章数据失败");
            }
        }else if(status.equals(WemediaConstants.WM_NEWS_AUTH_FAIL)){
            if(dto.getMsg()==null){
                wmNews.setReason("");
            }else {
                wmNews.setReason(dto.getMsg());
            }

            wmNews.setStatus(WemediaConstants.WM_NEWS_AUTH_FAIL);
            wmNewsMapper.updateById(wmNews);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 保存app端相关的文章数据
     * @param wmNews
     */
    @Autowired
    private WmChannelMapper wmChannelMapper;

    @Autowired
    private WmUserMapper wmUserMapper;

    @Qualifier("jmu.lsk.apis.article.IArticleClient")
    @Autowired
    private IArticleClient articleClient;

    private ResponseResult saveAppArticle(WmNews wmNews) {

        ArticleDto dto = new ArticleDto();
        //属性的拷贝
        BeanUtils.copyProperties(wmNews,dto);
        //文章的布局
        dto.setLayout(wmNews.getType());
        //频道
        WmChannel wmChannel = wmChannelMapper.selectById(wmNews.getChannelId());
        if(wmChannel != null){
            dto.setChannelName(wmChannel.getName());
        }

        //作者
        dto.setAuthorId(wmNews.getUserId().longValue());
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        if(wmUser != null){
            dto.setAuthorName(wmUser.getName());
        }

        //设置文章id
        if(wmNews.getArticleId() != null){
            dto.setId(wmNews.getArticleId());
        }
        dto.setCreatedTime(new Date());

        ResponseResult responseResult = articleClient.saveArticle(dto);
        return responseResult;
    }
}