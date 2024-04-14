package jmu.lsk.model.wemedia.dtos;

import jmu.lsk.model.common.dtos.PageRequestDto;
import lombok.Data;
 
import java.util.Date;
 
@Data
public class WmNewsPageReqDto extends PageRequestDto {
 
    /**
     * 状态
     */
    private Short status;
    /**
     * 开始时间
     */
    private Date beginPubDate;
    /**
     * 结束时间
     */
    private Date endPubDate;
    /**
     * 所属频道ID
     */
    private Integer channelId;
    /**
     * 关键字
     */
    private String keyword;
}