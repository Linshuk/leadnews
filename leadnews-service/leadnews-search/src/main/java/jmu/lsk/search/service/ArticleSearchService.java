package jmu.lsk.search.service;
import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.search.dtos.UserSearchDto;

import java.io.IOException;
 
public interface ArticleSearchService {
    /**
     ES文章分页搜索
     @return
     */
    ResponseResult search(UserSearchDto userSearchDto) throws IOException;
}