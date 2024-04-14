 
package jmu.lsk.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.user.dtos.LoginDto;
import jmu.lsk.model.user.pojos.ApUser;

public interface ApUserService extends IService<ApUser> {
 
    /**
     * app端登录
     * @param dto
     * @return
     */
    public ResponseResult login(LoginDto dto);
    
}