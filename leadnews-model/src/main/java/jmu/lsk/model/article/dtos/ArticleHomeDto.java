package jmu.lsk.model.article.dtos;
 
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;
 
@Data
public class ArticleHomeDto {
    /**
     * 最大时间
      */
    @Schema(description = "最大时间")
    Date maxBehotTime;


    /**
     * 最小时间
     */
    @Schema(description = "最小时间")
    Date minBehotTime;


    /**
     * 分页size
     */
    @Schema(description = "分页size")
    Integer size;

    /**
     * 频道id
     */
    @Schema(description = "频道id")
    String tag;
}