package jmu.lsk.wemedia.service;


import com.baomidou.mybatisplus.extension.service.IService;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.wemedia.pojos.WmChannel;

public interface WmChannelService extends IService<WmChannel> {
 
    /**
     * 查询所有频道
     * @return
     */
    public ResponseResult findAll();
 
 
}