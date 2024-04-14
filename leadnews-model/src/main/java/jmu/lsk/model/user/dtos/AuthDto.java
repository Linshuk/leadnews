package jmu.lsk.model.user.dtos;

import jmu.lsk.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class AuthDto extends PageRequestDto {
    private Integer id;

    private String msg;

    private Integer status;
}
