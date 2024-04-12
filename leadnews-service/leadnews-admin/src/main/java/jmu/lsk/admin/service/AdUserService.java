package jmu.lsk.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jmu.lsk.model.common.admin.dtos.AdLoginDto;
import jmu.lsk.model.common.admin.pojos.AdUser;
import jmu.lsk.model.common.dtos.ResponseResult;
import org.springframework.stereotype.Service;


public interface AdUserService extends IService<AdUser> {

    public ResponseResult login(AdLoginDto dto);
}
