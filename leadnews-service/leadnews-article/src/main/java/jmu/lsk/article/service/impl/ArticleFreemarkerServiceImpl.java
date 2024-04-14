package jmu.lsk.article.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jmu.lsk.article.service.ApArticleService;
import jmu.lsk.article.service.ArticleFreemarkerService;
import jmu.lsk.common.constants.ArticleConstants;
import jmu.lsk.file.service.FileStorageService;
import jmu.lsk.model.article.pojos.ApArticle;
import jmu.lsk.model.search.vos.SearchArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
 
@Service
@Slf4j
@Transactional
public class ArticleFreemarkerServiceImpl implements ArticleFreemarkerService {

 
    @Autowired
    private Configuration configuration;
 
    @Autowired
    private FileStorageService fileStorageService;
 
    @Autowired
    private ApArticleService apArticleService;
 
    /**
     * 生成静态文件上传到minIO中
     * @param apArticle
     * @param content
     */
    @Async
    @Override
    public void buildArticleToMinIO(ApArticle apArticle, String content) {
        //已知文章的id
        //4.1 获取文章内容
        if(StringUtils.isNotBlank(content)){
            //4.2 文章内容通过freemarker生成html文件
            Template template = null;
            StringWriter out = new StringWriter();
            try {
                template = configuration.getTemplate("article.ftl");
                //数据模型
                Map<String,Object> contentDataModel = new HashMap<>();
                contentDataModel.put("content", JSONArray.parseArray(content));
                //合成
                template.process(contentDataModel,out);
            } catch (Exception e) {
                e.printStackTrace();
            }
 
            //4.3 把html文件上传到minio中
            InputStream in = new ByteArrayInputStream(out.toString().getBytes());
            String path = fileStorageService.uploadHtmlFile("", apArticle.getId() + ".html", in);
 
 
            //4.4 修改ap_article表，保存static_url字段
            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId,apArticle.getId())
                    .set(ApArticle::getStaticUrl,path));

            createArticleESIndex(apArticle,content,path);
        }
    }

    @Autowired
    private KafkaTemplate kafkaTemplate;

    private void createArticleESIndex(ApArticle apArticle, String content, String path) {
        SearchArticleVo searchArticleVo = new SearchArticleVo();
        BeanUtils.copyProperties(apArticle,searchArticleVo);
        searchArticleVo.setContent(content);
        searchArticleVo.setStaticUrl(path);

        kafkaTemplate.send(ArticleConstants.ARTICLE_ES_SYNC_TOPIC,JSON.toJSONString(searchArticleVo));
    }
}