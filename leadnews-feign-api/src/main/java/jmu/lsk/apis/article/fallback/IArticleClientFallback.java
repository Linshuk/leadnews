package jmu.lsk.apis.article.fallback;
import jmu.lsk.apis.article.IArticleClient;
import jmu.lsk.model.common.article.dtos.ArticleDto;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Component;
 
/**
 * feign失败配置
 * @author itheima
 */
@Component
public class IArticleClientFallback implements IArticleClient {
    @Override
    public ResponseResult saveArticle(ArticleDto dto)  {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"获取数据失败");
    }
}