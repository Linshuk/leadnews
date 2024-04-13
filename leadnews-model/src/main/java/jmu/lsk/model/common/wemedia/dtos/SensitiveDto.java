package jmu.lsk.model.common.wemedia.dtos;

import jmu.lsk.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class SensitiveDto extends PageRequestDto {
    private String name;
}
