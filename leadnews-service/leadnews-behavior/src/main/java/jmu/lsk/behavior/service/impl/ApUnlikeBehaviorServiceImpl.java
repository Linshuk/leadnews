package jmu.lsk.behavior.service.impl;
import java.util.Date;

import jmu.lsk.behavior.service.ApUnlikeBehaviorService;
import jmu.lsk.common.exception.CustException;
import jmu.lsk.model.behavior.dtos.UnLikesBehaviorDTO;
import jmu.lsk.model.behavior.pojos.ApBehaviorEntry;
import jmu.lsk.model.behavior.pojos.ApUnlikesBehavior;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.enums.AppHttpCodeEnum;
import jmu.lsk.model.user.pojos.ApUser;
import jmu.lsk.utils.thread.AppThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
@Service
public class ApUnlikeBehaviorServiceImpl implements ApUnlikeBehaviorService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ResponseResult unlikeBehavior(UnLikesBehaviorDTO dto) {
        //1. 校验参数
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }

        //3. 查询不喜欢行为是否已经存在
        Query query = Query.query(Criteria.where("entryId").is(user.getId()).and("articleId").is(dto.getArticleId()));
        ApUnlikesBehavior unlikesBehavior = mongoTemplate.findOne(query, ApUnlikesBehavior.class);
        if(unlikesBehavior != null && dto.getType().intValue() == 0 ){
            CustException.cust(AppHttpCodeEnum.DATA_EXIST,"您已设置不喜欢");
        }
        //4. 添加不喜欢行为
        if(dto.getType().intValue() == 0){
            unlikesBehavior = new ApUnlikesBehavior();
            unlikesBehavior.setEntryId(user.getId());
            unlikesBehavior.setArticleId(dto.getArticleId());
            unlikesBehavior.setType((short)0);
            unlikesBehavior.setCreatedTime(new Date());
            mongoTemplate.save(unlikesBehavior);
        }else {
            //5. 取消不喜欢行为
            mongoTemplate.remove(query,ApUnlikesBehavior.class);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
