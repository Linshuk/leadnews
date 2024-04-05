package jmu.lsk.model.common.article.dtos;

import jmu.lsk.model.common.article.pojos.ApArticle;
import lombok.Data;
 
@Data
public class ArticleDto  extends ApArticle {
    /**
     * 文章内容
     */
    private String content;
}