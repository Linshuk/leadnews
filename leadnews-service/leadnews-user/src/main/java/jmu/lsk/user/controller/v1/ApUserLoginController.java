 
package jmu.lsk.user.controller.v1;

import com.alibaba.fastjson2.JSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.user.dtos.LoginDto;
import jmu.lsk.user.service.ApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/login")
@Tag(name = "app端用户登录",description = "app端用户登录API")
public class ApUserLoginController {

    @Autowired
    private ApUserService apUserService;

    @PostMapping("/login_auth")
    @Operation(summary = "用户登录")
    public ResponseResult login(LoginDto dto){
        return apUserService.login(dto);
    }
}