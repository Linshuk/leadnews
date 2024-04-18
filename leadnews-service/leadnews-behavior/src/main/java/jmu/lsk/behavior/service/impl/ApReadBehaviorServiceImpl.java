package jmu.lsk.behavior.service.impl;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import io.swagger.v3.core.util.Json;
import jmu.lsk.behavior.service.ApReadBehaviorService;
import jmu.lsk.common.constants.ArticleConstants;
import jmu.lsk.common.constants.HotArticleConstants;
import jmu.lsk.model.behavior.dtos.ReadBehaviorDTO;
import jmu.lsk.model.behavior.pojos.ApBehaviorEntry;
import jmu.lsk.model.behavior.pojos.ApReadBehavior;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.enums.AppHttpCodeEnum;
import jmu.lsk.model.mess.UpdateArticleMess;
import jmu.lsk.model.user.pojos.ApUser;
import jmu.lsk.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class ApReadBehaviorServiceImpl implements ApReadBehaviorService {

    @Autowired
    MongoTemplate mongoTemplate;
//    @Autowired
//    RabbitTemplate rabbitTemplate;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public ResponseResult readBehavior(ReadBehaviorDTO dto) {
        // 1. 校验参数
        ApUser user = AppThreadLocalUtil.getUser();

        // 3. 查询指定文章阅读行为是否存在
        Query query = Query.query(Criteria.where("entryId").is(user.getId()).and("articleId").is(dto.getArticleId()));
        ApReadBehavior readBehavior = mongoTemplate.findOne(query, ApReadBehavior.class);
        if(readBehavior == null){
            // 不存在    新增阅读行为
            readBehavior = new ApReadBehavior();
            readBehavior.setEntryId(user.getId());
            readBehavior.setArticleId(dto.getArticleId());
            readBehavior.setCount((short)1);
            readBehavior.setCreatedTime(new Date());
            readBehavior.setUpdatedTime(new Date());
            mongoTemplate.save(readBehavior);
        }else {
            // 存在     更新阅读次数
            UpdateArticleMess updateArticleMess = new UpdateArticleMess();
            updateArticleMess.setArticleId(dto.getArticleId());
            updateArticleMess.setAdd(1);
            updateArticleMess.setType(UpdateArticleMess.UpdateArticleType.VIEWS);
            kafkaTemplate.send(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC, JSON.toJSONString(updateArticleMess));

            readBehavior.setCount((short)(readBehavior.getCount() + 1));
            mongoTemplate.save(readBehavior);
        }
//        NewBehaviorDTO newBehavior = new NewBehaviorDTO();
//        newBehavior.setType(NewBehaviorDTO.BehaviorType.VIEWS);
//        newBehavior.setArticleId(dto.getArticleId());
//        newBehavior.setAdd(1);
//        rabbitTemplate.convertAndSend(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_QUEUE,JSON.toJSONString(newBehavior));
//        log.info("成功发送 文章阅读行为消息 , 消息内容: {}",newBehavior);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
