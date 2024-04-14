package jmu.lsk.model.wemedia.dtos;

import jmu.lsk.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class WmMaterialDto extends PageRequestDto {
    /**
     * 1 收藏
     * 0 未收藏
     */
    private Short isCollection;
}