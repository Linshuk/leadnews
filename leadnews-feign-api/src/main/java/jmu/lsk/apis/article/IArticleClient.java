package jmu.lsk.apis.article;
import jmu.lsk.apis.article.fallback.IArticleClientFallback;
import jmu.lsk.model.article.dtos.ArticleDto;
import jmu.lsk.model.behavior.dtos.ArticleBehaviorDTO;
import jmu.lsk.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient(value = "leadnews-article",fallback = IArticleClientFallback.class)
public interface IArticleClient {
 
    @PostMapping("/api/v1/article/save")
    public ResponseResult saveArticle(@RequestBody ArticleDto dto);


}