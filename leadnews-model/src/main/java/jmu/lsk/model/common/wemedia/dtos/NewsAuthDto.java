package jmu.lsk.model.common.wemedia.dtos;

import io.swagger.v3.oas.models.security.SecurityScheme;
import jmu.lsk.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class NewsAuthDto extends PageRequestDto {
    private Integer id;

    private String msg;

    private Integer status;

    private String title;
}
