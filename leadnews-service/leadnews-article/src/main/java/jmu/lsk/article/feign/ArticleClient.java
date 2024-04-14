package jmu.lsk.article.feign;
import jmu.lsk.apis.article.IArticleClient;
import jmu.lsk.article.service.ApArticleService;
import jmu.lsk.model.article.dtos.ArticleDto;
import jmu.lsk.model.behavior.dtos.ArticleBehaviorDTO;
import jmu.lsk.model.behavior.dtos.CollectionBehaviorDTO;
import jmu.lsk.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleClient implements IArticleClient {
 
    @Autowired
    private ApArticleService apArticleService;
 
    @Override
    @PostMapping("/api/v1/article/save")
    public ResponseResult saveArticle(@RequestBody ArticleDto dto) {
        return apArticleService.saveArticle(dto);
    }


}