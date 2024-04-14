package jmu.lsk.user.controller.v1;

import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.user.dtos.UserRelationDto;
import jmu.lsk.user.service.ApUserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class ApUserRelationController {

    @Autowired
    private ApUserRelationService apUserRelationService;
  
    @PostMapping("/user_follow")
    public ResponseResult follow(@RequestBody UserRelationDto dto){
        return apUserRelationService.follow(dto);
    }
}
