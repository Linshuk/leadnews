package jmu.lsk.model.wemedia.dtos;

import jmu.lsk.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class ChannelDto extends PageRequestDto {

    private String name;
}
