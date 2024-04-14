package jmu.lsk.behavior.service;


import jmu.lsk.model.behavior.dtos.CollectionBehaviorDTO;
import jmu.lsk.model.common.dtos.ResponseResult;

public interface ApCollectionBehaviorService {
    /**
     * 收藏 取消收藏
     * @param dto
     * @return
     */
    ResponseResult collectBehavior(CollectionBehaviorDTO dto);
}