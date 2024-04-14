package jmu.lsk.behavior.controller.v1;

import jmu.lsk.behavior.service.*;
import jmu.lsk.model.behavior.dtos.*;
import jmu.lsk.model.common.dtos.ResponseResult;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BehaviorController{


    @Autowired
    private ApLikesBehaviorService apLikesBehaviorService;

    @PostMapping("/likes_behavior")
    public ResponseResult saveLikesBehavior(@RequestBody LikesBehaviorDTO dto) {
        return apLikesBehaviorService.like(dto);
    }

    @Autowired
    private ApUnlikeBehaviorService apUnlikeBehaviorService;

    @PostMapping("/un_likes_behavior")
    public ResponseResult saveUnlikesBehavior(@RequestBody UnLikesBehaviorDTO dto) {
        return apUnlikeBehaviorService.unlikeBehavior(dto);
    }

    @Autowired
    private ApReadBehaviorService apReadBehaviorService;

    @PostMapping("/read_behavior")
    public ResponseResult saveReadBehavior(@RequestBody ReadBehaviorDTO dto) {
        return apReadBehaviorService.readBehavior(dto);
    }

    @Autowired
    ApCollectionBehaviorService apCollectionBehaviorService;

    @PostMapping("collection_behavior")
    public ResponseResult collectionReadBehavior(@RequestBody CollectionBehaviorDTO dto) {
        return apCollectionBehaviorService.collectBehavior(dto);
    }

}