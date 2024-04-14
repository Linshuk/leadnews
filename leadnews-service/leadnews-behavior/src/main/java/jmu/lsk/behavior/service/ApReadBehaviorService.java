package jmu.lsk.behavior.service;



import jmu.lsk.model.behavior.dtos.ReadBehaviorDTO;
import jmu.lsk.model.common.dtos.ResponseResult;

public interface ApReadBehaviorService{
    /**
     * 记录阅读行为
     * @param dto
     * @return
     */
    ResponseResult readBehavior(ReadBehaviorDTO dto);
}