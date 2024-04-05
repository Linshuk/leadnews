package jmu.lsk.article.service;


import jmu.lsk.model.common.article.pojos.ApArticle;

public interface ArticleFreemarkerService {
 
    /**
     * 生成静态文件上传到minIO中
     * @param apArticle
     * @param content
     */
    public void buildArticleToMinIO(ApArticle apArticle, String content);
}