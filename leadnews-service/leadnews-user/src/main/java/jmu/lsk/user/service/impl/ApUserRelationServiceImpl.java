package jmu.lsk.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jmu.lsk.common.exception.CustException;
import jmu.lsk.common.redis.CacheService;
import jmu.lsk.model.common.constant.UserRelationConstants;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.enums.AppHttpCodeEnum;
import jmu.lsk.model.user.dtos.UserRelationDto;
import jmu.lsk.model.user.pojos.ApUser;
import jmu.lsk.model.user.pojos.ApUserFan;
import jmu.lsk.model.user.pojos.ApUserFollow;
import jmu.lsk.model.wemedia.pojos.WmUser;
import jmu.lsk.user.mapper.ApUserFanMapper;
import jmu.lsk.user.mapper.ApUserFollowMapper;
import jmu.lsk.user.mapper.ApUserMapper;
import jmu.lsk.user.service.ApUserRelationService;
import jmu.lsk.utils.thread.AppThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Date;


@Service
@Transactional
public class ApUserRelationServiceImpl extends ServiceImpl<ApUserFollowMapper,ApUserFollow> implements ApUserRelationService {

//    @Autowired
//    StringRedisTemplate redisTemplate;
    @Autowired
    CacheService cacheService;

    @Override
    public ResponseResult follow(UserRelationDto dto) {
        // 1. 检查参数  (是否登陆   operation: 0 1   自己不能关注自己   是否已经关注)
        ApUser loginUser = AppThreadLocalUtil.getUser();

        if (loginUser == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }
        int operation = dto.getOperation().intValue();
        if (operation != 0 && operation != 1) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID, "关注类型错误  类型必须为: 0关注  1取关");
        }
        // 用于操作zset接口的 redis对象
//        ZSetOperations<String, String> zsetOption = redisTemplate.opsForZSet();

        // 开启支持事务
        if (operation == 0) {
            // 2. operation = 0  关注     关注集合   粉丝集合 添加数据
            if (loginUser.getId().equals(dto.getAuthorId())) {
                CustException.cust(AppHttpCodeEnum.DATA_NOT_ALLOW, "自己不能关注自己");
            }

            try {
                // zscore  集合   元素      有返回值: 有这个元素    返回值null: 没有这个元素
                Double score = cacheService.zScore(UserRelationConstants.FOLLOW_LIST + loginUser.getId(), String.valueOf(dto.getAuthorId()));
                if (score != null) {
                    CustException.cust(AppHttpCodeEnum.DATA_NOT_ALLOW, "请勿重复关注");
                }

                // 将作者id 添加到我的关注集合中

                cacheService.zAdd(UserRelationConstants.FOLLOW_LIST + loginUser.getId(), String.valueOf(dto.getAuthorId()), System.currentTimeMillis());
                cacheService.zAdd(UserRelationConstants.FANS_LIST + dto.getAuthorId(), String.valueOf(loginUser.getId()), System.currentTimeMillis());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 3. operation = 1  取关     关注集合   粉丝集合 删除数据
            cacheService.zRemove(UserRelationConstants.FOLLOW_LIST + loginUser.getId(), String.valueOf(dto.getAuthorId()));
            cacheService.zRemove(UserRelationConstants.FANS_LIST + dto.getAuthorId(), String.valueOf(loginUser.getId()));
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

}
