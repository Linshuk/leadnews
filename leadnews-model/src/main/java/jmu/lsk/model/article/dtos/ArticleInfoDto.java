package jmu.lsk.model.article.dtos;

import jmu.lsk.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class ArticleInfoDto {

    @IdEncrypt
    private Long articleId;

    private int authorId;
}
