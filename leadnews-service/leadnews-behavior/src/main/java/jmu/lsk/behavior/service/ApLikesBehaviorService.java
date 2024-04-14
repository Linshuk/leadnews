package jmu.lsk.behavior.service;


import jmu.lsk.model.behavior.dtos.LikesBehaviorDTO;
import jmu.lsk.model.common.dtos.ResponseResult;

public interface ApLikesBehaviorService {
    /**
     * 点赞或取消点赞
     * @param dto
     * @return
     */
	public ResponseResult like(LikesBehaviorDTO dto);
}