package jmu.lsk.behavior.service.impl;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import jmu.lsk.behavior.service.ApCollectionBehaviorService;
import jmu.lsk.common.constants.BehaviorConstants;
import jmu.lsk.common.exception.CustException;
import jmu.lsk.model.behavior.dtos.CollectionBehaviorDTO;
import jmu.lsk.model.behavior.pojos.ApBehaviorEntry;
import jmu.lsk.model.behavior.pojos.ApCollection;
import jmu.lsk.model.behavior.vos.BehaviorVo;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.enums.AppHttpCodeEnum;
import jmu.lsk.model.user.pojos.ApUser;
import jmu.lsk.utils.thread.AppThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
public class ApCollectionBehaviorServiceImpl implements ApCollectionBehaviorService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public ResponseResult collectBehavior(CollectionBehaviorDTO dto) {
        // 1. 校验参数
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 2. 查询行为实体

        Query query = Query.query(Criteria.where("entryId").is(user.getId()).and("articleId").is(dto.getArticleId()));
        // 3. 如果是收藏操作，查询是否已经收藏
        if(dto.getOperation().intValue() == 0){
            ApCollection apCollection = mongoTemplate.findOne(query, ApCollection.class);
            if(apCollection!=null){
                CustException.cust(AppHttpCodeEnum.DATA_EXIST,"您已收藏该文章");
            }
            apCollection = new ApCollection();
            apCollection.setEntryId(user.getId());
            apCollection.setArticleId(dto.getArticleId());
            apCollection.setType((short)0);
            apCollection.setCollectionTime(new Date());
            mongoTemplate.save(apCollection);
        }else {
            mongoTemplate.remove(query,ApCollection.class);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}