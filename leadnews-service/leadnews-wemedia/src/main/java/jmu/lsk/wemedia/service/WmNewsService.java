package jmu.lsk.wemedia.service;


import com.baomidou.mybatisplus.extension.service.IService;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.wemedia.dtos.WmNewsPageReqDto;
import jmu.lsk.model.common.wemedia.pojos.WmNews;

public interface WmNewsService extends IService<WmNews> {
 
    /**
     * 查询文章
     * @param dto
     * @return
     */
    public ResponseResult findAll(WmNewsPageReqDto dto);
 
}