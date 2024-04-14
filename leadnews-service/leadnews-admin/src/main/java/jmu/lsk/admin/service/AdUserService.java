package jmu.lsk.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jmu.lsk.model.admin.dtos.AdLoginDto;
import jmu.lsk.model.admin.pojos.AdUser;
import jmu.lsk.model.common.dtos.ResponseResult;


public interface AdUserService extends IService<AdUser> {

    public ResponseResult login(AdLoginDto dto);
}
