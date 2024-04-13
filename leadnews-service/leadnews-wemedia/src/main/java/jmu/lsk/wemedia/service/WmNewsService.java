package jmu.lsk.wemedia.service;


import com.baomidou.mybatisplus.extension.service.IService;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.wemedia.dtos.NewsAuthDto;
import jmu.lsk.model.common.wemedia.dtos.WmNewsDto;
import jmu.lsk.model.common.wemedia.dtos.WmNewsPageReqDto;
import jmu.lsk.model.common.wemedia.pojos.WmNews;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface WmNewsService extends IService<WmNews> {
 
    /**
     * 查询文章
     * @param dto
     * @return
     */
    public ResponseResult findAll(WmNewsPageReqDto dto);


    /**
     *  发布文章或保存草稿
     * @param dto
     * @return
     */
    public ResponseResult submitNews(WmNewsDto dto);

    /**
     * 消费延迟队列数据
     */
    public void scanNewsByTask();


    /**
     * 文章的上下架
     * @param dto
     * @return
     */
    public ResponseResult downOrUp(WmNewsDto dto);

    public ResponseResult collect(Integer id);

    public ResponseResult deleteNews(Integer id);


    public ResponseResult findList(NewsAuthDto dto);

    public ResponseResult findWmNewsVo(Integer id);

    public ResponseResult updateStatus(Short status,NewsAuthDto dto);

}