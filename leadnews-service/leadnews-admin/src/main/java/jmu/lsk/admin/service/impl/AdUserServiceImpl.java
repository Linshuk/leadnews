package jmu.lsk.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jmu.lsk.admin.mapper.AdUserMapper;
import jmu.lsk.admin.service.AdUserService;
import jmu.lsk.model.common.admin.dtos.AdLoginDto;
import jmu.lsk.model.common.admin.pojos.AdUser;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.enums.AppHttpCodeEnum;
import jmu.lsk.utils.common.AppJwtUtil;
import jmu.lsk.utils.common.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.sql.Wrapper;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper, AdUser> implements AdUserService {
    @Override
    public ResponseResult login(AdLoginDto dto) {
        if(!StringUtils.isBlank(dto.getName())&&!StringUtils.isBlank(dto.getPassword())){
            AdUser adUser = getOne(Wrappers.<AdUser>lambdaQuery().eq(AdUser::getName,dto.getName()));
            if(adUser == null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户信息不存在");
            }

            String salt = adUser.getSalt();
            String pswd = dto.getPassword();
            pswd = DigestUtils.md5DigestAsHex((salt+pswd).getBytes());
            if(!pswd.equals(adUser.getPassword())){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }
            Map<String,Object> map = new HashMap<>();
            map.put("token", AppJwtUtil.getToken(adUser.getId().longValue()));
            adUser.setPassword("");
            adUser.setSalt("");
            map.put("user",adUser);

            return ResponseResult.okResult(map);
        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户信息不存在");
        }

    }
}
