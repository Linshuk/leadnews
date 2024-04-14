package jmu.lsk.search.service;

import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.search.dtos.HistorySearchDto;

public interface ApUserSearchService {
 
    /**
     * 保存用户搜索历史记录
     * @param keyword
     * @param userId
     */
    public void insert(String keyword,Integer userId);

    public ResponseResult findUserSearch();


    /**
     删除搜索历史
     @param historySearchDto
     @return
     */
    ResponseResult delUserSearch(String historySearchDto);
}