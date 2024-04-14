package jmu.lsk.article.service.impl;

import jmu.lsk.article.service.ApArticleBehaviorService;
import jmu.lsk.common.exception.CustException;
import jmu.lsk.common.redis.CacheService;
import jmu.lsk.model.behavior.dtos.ArticleBehaviorDTO;
import jmu.lsk.model.behavior.pojos.ApBehaviorEntry;
import jmu.lsk.model.behavior.pojos.ApCollection;
import jmu.lsk.model.behavior.pojos.ApLikesBehavior;
import jmu.lsk.model.behavior.pojos.ApUnlikesBehavior;
import jmu.lsk.model.common.constant.UserRelationConstants;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.enums.AppHttpCodeEnum;
import jmu.lsk.model.user.pojos.ApUser;
import jmu.lsk.utils.thread.AppThreadLocalUtil;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ApArticleBehaviorServiceImpl implements ApArticleBehaviorService {
//    @Autowired
//    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private CacheService cacheService;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public ResponseResult loadArticleBehavior(ArticleBehaviorDTO dto) {
        //1.检查参数
        //{ "isfollow": true, "islike": true,"isunlike": false,"iscollection": true }
        boolean isfollow = false, islike = false, isunlike = false, iscollection = false;
        ApUser user = AppThreadLocalUtil.getUser();
        if(user != null){

            Query query = Query.query(Criteria.where("entryId").is(user.getId()).and("articleId").is(dto.getArticleId()));
            //喜欢行为
            ApLikesBehavior likesBehavior = mongoTemplate.findOne(query, ApLikesBehavior.class);
            if (likesBehavior != null) {
                    islike = true;
            }
            //不喜欢行为
            ApUnlikesBehavior unlikesBehavior = mongoTemplate.findOne(query, ApUnlikesBehavior.class);
            if (unlikesBehavior != null) {
                    isunlike = true;
            }
            //是否收藏
            ApCollection apCollection = mongoTemplate.findOne(query, ApCollection.class);
            if (apCollection != null) {
                iscollection = true;
            }
            //是否关注
            Double score = cacheService.zScore((UserRelationConstants.FOLLOW_LIST + String.valueOf(user.getId())),dto.getAuthorId().toString());
            //            Double score = redisTemplate.opsForZSet().score(UserRelationConstants.FOLLOW_LIST + user.getId(),dto.getAuthorId().toString());
            if(score != null){
                isfollow = true;
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isfollow", isfollow);
        resultMap.put("islike", islike);
        resultMap.put("isunlike", isunlike);
        resultMap.put("iscollection", iscollection);
        return ResponseResult.okResult(resultMap);
    }
}