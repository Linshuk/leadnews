package jmu.lsk.admin.controller.v1;

import jmu.lsk.admin.service.AdUserService;
import jmu.lsk.model.admin.dtos.AdLoginDto;
import jmu.lsk.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AdUserLoginController {

    @Autowired
    private AdUserService adUserService;

    @PostMapping("/in")
    public ResponseResult login(@RequestBody AdLoginDto dto){
        return adUserService.login(dto);
    }


}
