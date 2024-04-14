package jmu.lsk.article;
 
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jmu.lsk.article.mapper.ApArticleContentMapper;
import jmu.lsk.article.mapper.ApArticleMapper;
import jmu.lsk.file.service.FileStorageService;
import jmu.lsk.model.article.pojos.ApArticle;
import jmu.lsk.model.article.pojos.ApArticleContent;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class ArticleFreemarkerTest {
 
    @Autowired
    private Configuration configuration;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
 
    @Test
    public void createStaticUrlTest() throws Exception {
        //1.获取文章内容
        ApArticleContent apArticleContent = apArticleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, 1383827787629252610L));
        if(apArticleContent != null && StringUtils.isNotBlank(apArticleContent.getContent())){
           //2.文章内容通过freemarker生成html文件
            StringWriter out = new StringWriter();
            Template template = configuration.getTemplate("article.ftl");

            //第一个参数 数据模型
            Map<String, Object> params = new HashMap<>();
            //JSONArray.parseArray 字符串转成对象
            params.put("content", JSONArray.parseArray(apArticleContent.getContent()));
            //合成
            template.process(params, out);
            //输入流
            InputStream is = new ByteArrayInputStream(out.toString().getBytes());

            //3.把html文件上传到minio中    （前缀，文件名称，输入流）
            String path = fileStorageService.uploadHtmlFile("",
                    apArticleContent.getArticleId() + ".html", is);

            //4.修改ap_article表，保存static_url字段
            ApArticle article = new ApArticle();
            article.setId(apArticleContent.getArticleId());
            article.setStaticUrl(path);
            apArticleMapper.updateById(article);

        }
    }

    /**
     * 生产者
     */
    @Test

        public  void sss() {
            //1.kafka的配置信息
            Properties properties = new Properties();
            //kafka的连接地址
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.200.130:9092");
            //发送失败，失败的重试次数
            properties.put(ProducerConfig.RETRIES_CONFIG,5);
            //消息key的序列化器
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
            //消息value的序列化器
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

            //2.生产者对象
            KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

            //封装发送的消息
            ProducerRecord<String,String> record = new ProducerRecord<String, String>("itheima-topic","100001","hello kafka");

            //3.发送消息
            producer.send(record);

            //4.关闭消息通道，必须关闭，否则消息发送不成功
            producer.close();
        }

}