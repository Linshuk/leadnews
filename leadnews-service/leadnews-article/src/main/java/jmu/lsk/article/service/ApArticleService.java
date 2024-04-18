package jmu.lsk.article.service;


import com.baomidou.mybatisplus.extension.service.IService;
import jmu.lsk.model.article.dtos.ArticleDto;
import jmu.lsk.model.article.dtos.ArticleHomeDto;
import jmu.lsk.model.article.dtos.ArticleInfoDto;
import jmu.lsk.model.article.pojos.ApArticle;
import jmu.lsk.model.common.dtos.ResponseResult;

import java.util.Date;
import java.util.List;

public interface ApArticleService extends IService<ApArticle> {
 
    /**
     * 根据参数加载文章列表
     * @param loadtype 1为加载更多  2为加载最新
     * @param dto
     * @return
     */
    ResponseResult load(Short loadtype, ArticleHomeDto dto);

    ResponseResult saveArticle(ArticleDto dto);

}