package jmu.lsk.model.article.vos;
 

import jmu.lsk.model.article.pojos.ApArticle;
import lombok.Data;
 
@Data
public class HotArticleVo extends ApArticle {
    /**
     * 文章分值
     */
    private Integer score;
}