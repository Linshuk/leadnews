package jmu.lsk.search.controller.v1;

import com.alibaba.fastjson2.JSON;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.search.dtos.HistorySearchDto;
import jmu.lsk.search.service.ApUserSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * APP用户搜索信息表 前端控制器
 * </p>
 * @author itheima
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/history")
public class ApUserSearchController{
 
    @Autowired
    private ApUserSearchService apUserSearchService;
 
    @PostMapping("/load")
    public ResponseResult findUserSearch() {
        return apUserSearchService.findUserSearch();
    }


    @PostMapping("/del")
    public ResponseResult delUserSearch(@RequestBody String historySearchDto) {
        Map<String,String> map = JSON.parseObject(historySearchDto, Map.class);
        String id = map.get("id");
        return apUserSearchService.delUserSearch(id);
    }
 
}