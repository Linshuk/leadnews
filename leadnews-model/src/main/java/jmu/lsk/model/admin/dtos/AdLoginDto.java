package jmu.lsk.model.admin.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class AdLoginDto {

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String name;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;
}
