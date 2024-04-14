package jmu.lsk.es;
import com.alibaba.fastjson2.JSON;
import jmu.lsk.es.mapper.ApArticleMapper;
import jmu.lsk.es.pojo.SearchArticleVo;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApArticleTest {
 
    @Autowired
    private ApArticleMapper apArticleMapper;
 
    @Autowired
    private RestHighLevelClient restHighLevelClient;


    /**
     * 注意：数据量的导入，如果数据量过大，需要分页导入
     * @throws Exception
     */
    @Test
    public void init() throws Exception {
 
        //1.查询所有符合条件的文章数据
        List<SearchArticleVo> searchArticleVos = apArticleMapper.loadArticleList();
 
        //2.批量导入到es索引库

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");

        for (SearchArticleVo searchArticleVo : searchArticleVos) {

            bulkRequest.add(new IndexRequest("app_info_article")
                    .id(String.valueOf(searchArticleVo.getId()))
                    .source(JSON.toJSONString(searchArticleVo),XContentType.JSON));
        }

        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
        System.out.println(bulkResponse.hasFailures());
    }

    @Test
    public void sss()throws IOException {
        SearchRequest searchRequest = new SearchRequest("app_info_article");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        System.out.println((searchResponse.getHits().getTotalHits().value));
    }

    @Test
    public void add()throws IOException{

        SearchArticleVo searchArticleVo = apArticleMapper.loadArticle("央视曝光境外医疗豪华旅游套路");

        IndexRequest request = new IndexRequest("app_info_article");
        request.id(String.valueOf(searchArticleVo.getId()));
        request.source(JSON.toJSONString(searchArticleVo),XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(request,RequestOptions.DEFAULT);
        System.out.println(indexResponse.toString());
        System.out.println(indexResponse.status());


    }

}