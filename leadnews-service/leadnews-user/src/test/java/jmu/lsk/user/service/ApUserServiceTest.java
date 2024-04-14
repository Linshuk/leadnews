package jmu.lsk.user.service;

import jmu.lsk.common.redis.CacheService;
import jmu.lsk.model.common.constant.UserRelationConstants;
import jmu.lsk.model.user.dtos.LoginDto;
import jmu.lsk.user.UserApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;


    @SpringBootTest(classes = UserApplication.class)
    @RunWith(SpringRunner.class)
    public class ApUserServiceTest {

        @Autowired
        private ApUserService apUserService;


        @Test
        public void autoScanWmNews() {
            LoginDto dto = new LoginDto();
            dto.setPhone("12345678910");
            dto.setPassword("123456");
            apUserService.login(dto);
        }

        @Autowired
        StringRedisTemplate redisTemplate;
        @Autowired
        CacheService cacheService;

        @Test
        public  void redis(){
//            cacheService.zAdd(UserRelationConstants.FOLLOW_LIST + "8", "1111", System.currentTimeMillis());
//            redisTemplate.opsForZSet().add(UserRelationConstants.FOLLOW_LIST + "8", "1111", System.currentTimeMillis());
//            redisTemplate.setEnableTransactionSupport(true);
//            zsetOption.add(UserRelationConstants.FOLLOW_LIST + "8", "1111", System.currentTimeMillis());
//            zsetOption.add(UserRelationConstants.FANS_LIST + "1111", "8", System.currentTimeMillis());
        cacheService.zScore("apuser:follow:6","1102");

            System.out.println(cacheService.zScore("apuser:follow:6","1102"));
        }

    }
