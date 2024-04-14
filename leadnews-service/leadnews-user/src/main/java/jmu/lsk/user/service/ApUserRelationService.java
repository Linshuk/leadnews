package jmu.lsk.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.user.dtos.UserRelationDto;
import jmu.lsk.model.user.pojos.ApUserFollow;

public interface ApUserRelationService extends IService<ApUserFollow>{
    ResponseResult follow(UserRelationDto dto);
}
