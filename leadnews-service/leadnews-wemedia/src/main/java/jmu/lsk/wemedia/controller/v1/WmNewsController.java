package jmu.lsk.wemedia.controller.v1;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.enums.AppHttpCodeEnum;
import jmu.lsk.model.common.wemedia.dtos.WmNewsDto;
import jmu.lsk.model.common.wemedia.dtos.WmNewsPageReqDto;
import jmu.lsk.model.common.wemedia.pojos.WmNews;
import jmu.lsk.wemedia.service.WmNewsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {
 
 
    @Autowired
    private WmNewsService wmNewsService;
 
    @PostMapping("/list")
    public ResponseResult findAll(@RequestBody WmNewsPageReqDto dto){
        return  wmNewsService.findAll(dto);
    }

    @PostMapping("/submit")
    public ResponseResult submitNews(@RequestBody WmNewsDto dto){
        return wmNewsService.submitNews(dto);
    }

    @GetMapping("/one/{id}")
    public ResponseResult collect(@PathVariable Integer id){
        if(id==null)
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        WmNews wmNews = wmNewsService.getById(id);

        return ResponseResult.okResult(wmNews);
    }

    @GetMapping("/del_news/{id}")
    public ResponseResult deleteNews(@PathVariable Integer id){
        if(id==null)
            return ResponseResult.errorResult(501,"文章Id不可缺少");
        WmNews wmNews = wmNewsService.getById(id);

        if(wmNews==null){
            return ResponseResult.errorResult(1002,"文章不存在");
        }
        if(wmNews.getStatus()==9){
            return ResponseResult.errorResult(501,"文章已发布，不能删除");
        }
        wmNewsService.removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


    @PostMapping("/down_or_up")
    public ResponseResult downOrUp(@RequestBody WmNewsDto dto){
        return wmNewsService.downOrUp(dto);
    }

}