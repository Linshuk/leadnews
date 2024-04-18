package jmu.lsk.article.listener;
import com.alibaba.fastjson2.JSON;
import jmu.lsk.article.service.ApArticleService;
import jmu.lsk.common.constants.HotArticleConstants;
import jmu.lsk.model.article.mess.ArticleVisitStreamMess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
 
@Component
@Slf4j
public class ArticleIncrHandleListener {
 
    @Autowired
    private ApArticleService apArticleService;
 
    @KafkaListener(topics = HotArticleConstants.HOT_ARTICLE_INCR_HANDLE_TOPIC)
    public void onMessage(String mess){
        if(StringUtils.isNotBlank(mess)){
            ArticleVisitStreamMess articleVisitStreamMess = JSON.parseObject(mess, ArticleVisitStreamMess.class);
            apArticleService.updateScore(articleVisitStreamMess);
        }
    }
}