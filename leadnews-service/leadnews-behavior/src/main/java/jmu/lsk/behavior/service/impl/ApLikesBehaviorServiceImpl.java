package jmu.lsk.behavior.service.impl;

import java.util.Date;

import jmu.lsk.behavior.service.ApLikesBehaviorService;
import jmu.lsk.common.exception.CustException;
import jmu.lsk.model.behavior.dtos.LikesBehaviorDTO;
import jmu.lsk.model.behavior.pojos.ApBehaviorEntry;
import jmu.lsk.model.behavior.pojos.ApLikesBehavior;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.enums.AppHttpCodeEnum;
import jmu.lsk.model.user.pojos.ApUser;
import jmu.lsk.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class ApLikesBehaviorServiceImpl implements ApLikesBehaviorService {

    @Autowired
    MongoTemplate mongoTemplate;

//    @Autowired
//    RabbitTemplate rabbitTemplate;


    @Override
    public ResponseResult like(LikesBehaviorDTO dto) {
        // 1. 校验参数
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 2. 查询行为实体

        // 3. 查询是否有该行为实体的点赞记录 如果是点赞并且有记录 提示请勿重复点赞
        Query query = Query.query(Criteria.where("entryId").is(user.getId()).and("articleId").is(dto.getArticleId()));
        ApLikesBehavior likesBehavior = mongoTemplate.findOne(query, ApLikesBehavior.class);
        if(dto.getOperation().intValue() == 0 && likesBehavior!=null){
            CustException.cust(AppHttpCodeEnum.DATA_EXIST,"请勿重复点赞");
        }
        // 4. 点赞或取消点赞
        if(dto.getOperation().intValue() == 0){
            // 点赞   添加点赞数据
            likesBehavior = new ApLikesBehavior();
            likesBehavior.setEntryId(user.getId());
            likesBehavior.setArticleId(dto.getArticleId());
            likesBehavior.setType((short)0);
            likesBehavior.setOperation((short)0);
            likesBehavior.setCreatedTime(new Date());
            mongoTemplate.save(likesBehavior);
        }else {
            // 取消点赞  删除点赞数据
            mongoTemplate.remove(query, ApLikesBehavior.class);
        }
        // 发送新行为消息
//        NewBehaviorDTO newBehavior = new NewBehaviorDTO();
//        newBehavior.setType(BehaviorType.LIKES);
//        newBehavior.setArticleId(dto.getArticleId());
//        newBehavior.setAdd(dto.getOperation().intValue()==0?1:-1);
//        rabbitTemplate.convertAndSend(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_QUEUE,JSON.toJSONString(newBehavior));
//        log.info("成功发送 文章点赞行为消息 , 消息内容: {}",newBehavior);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
