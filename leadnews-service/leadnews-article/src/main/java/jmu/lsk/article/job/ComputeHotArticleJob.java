package jmu.lsk.article.job;
import com.xxl.job.core.handler.annotation.XxlJob;
import jmu.lsk.article.service.HotArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
@Component
@Slf4j
public class ComputeHotArticleJob {
 
    @Autowired
    private HotArticleService hotArticleService;
 
    @XxlJob("computeHotArticle")
    public void handle(){
        log.info("热文章分值计算调度任务开始执行...");
        hotArticleService.computeHotArticle();
        log.info("热文章分值计算调度任务结束...");
 
    }
}