package jmu.lsk.article.service;


import jmu.lsk.model.behavior.dtos.ArticleBehaviorDTO;
import jmu.lsk.model.common.dtos.ResponseResult;

public interface ApArticleBehaviorService {

    /**
     * 加载文章详情 数据回显
     * @param dto
     * @return
     */
    public ResponseResult loadArticleBehavior(ArticleBehaviorDTO dto);
}