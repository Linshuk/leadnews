package jmu.lsk.user.service.impl;

 
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.enums.AppHttpCodeEnum;
import jmu.lsk.model.user.dtos.LoginDto;
import jmu.lsk.model.user.pojos.ApUser;
import jmu.lsk.user.mapper.ApUserMapper;
import jmu.lsk.user.service.ApUserService;
import jmu.lsk.utils.common.AppJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
 
 
@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {

    @Override
    public ResponseResult login(LoginDto dto) {
            //1.正常登录（手机号+密码登录）
            if (!StringUtils.isBlank(dto.getPhone()) && !StringUtils.isBlank(dto.getPassword())) {
                //1.1查询用户
                ApUser apUser = getOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, dto.getPhone()));
                if (apUser == null) {
                    return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户不存在");
                }

                //1.2 比对密码
                String salt = apUser.getSalt();
                String pswd = dto.getPassword();
                pswd = DigestUtils.md5DigestAsHex((pswd + salt).getBytes());
                if (!pswd.equals(apUser.getPassword())) {
                    return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
                }
                //1.3 返回数据  jwt
                Map<String, Object> map = new HashMap<>();
                map.put("token", AppJwtUtil.getToken(apUser.getId().longValue()));
                //这两个值 置空 再返回对象
                apUser.setSalt("");
                apUser.setPassword("");
                map.put("user", apUser);
                return ResponseResult.okResult(map);
            }
            else {
                //2.游客  同样返回token  id = 0
                Map<String, Object> map = new HashMap<>();
                map.put("token", AppJwtUtil.getToken(0l));
                return ResponseResult.okResult(map);
            }
        }
    }