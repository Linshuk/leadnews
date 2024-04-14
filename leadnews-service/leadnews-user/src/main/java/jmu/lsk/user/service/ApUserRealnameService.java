package jmu.lsk.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.user.dtos.AuthDto;
import jmu.lsk.model.user.pojos.ApUserRealname;

public interface ApUserRealnameService extends IService<ApUserRealname> {
    ResponseResult loadListByStatus(AuthDto dto);

    ResponseResult updateStatus(AuthDto dto,Short operation);

}
