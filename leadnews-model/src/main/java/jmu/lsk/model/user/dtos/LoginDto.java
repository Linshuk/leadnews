package jmu.lsk.model.user.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class LoginDto {

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;
}
