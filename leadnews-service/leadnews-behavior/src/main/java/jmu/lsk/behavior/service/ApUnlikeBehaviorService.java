package jmu.lsk.behavior.service;


import jmu.lsk.model.behavior.dtos.UnLikesBehaviorDTO;
import jmu.lsk.model.common.dtos.ResponseResult;

public interface ApUnlikeBehaviorService {
    /**
     * 保存 或 取消 不喜欢
     * @param dto
     * @return
     */
    ResponseResult unlikeBehavior(UnLikesBehaviorDTO dto);
}
