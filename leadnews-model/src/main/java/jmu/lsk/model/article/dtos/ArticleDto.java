package jmu.lsk.model.article.dtos;

import jmu.lsk.model.article.pojos.ApArticle;
import lombok.Data;
 
@Data
public class ArticleDto  extends ApArticle {
    /**
     * 文章内容
     */
    private String content;
}