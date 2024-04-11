package jmu.lsk.search.service;

import jmu.lsk.model.common.dtos.ResponseResult;
import jmu.lsk.model.common.search.dtos.UserSearchDto;

/**
 * <p>
 * 联想词表 服务类
 * </p>
 *
 * @author itheima
 */
public interface ApAssociateWordsService {
 
    /**
     联想词
     @param userSearchDto
     @return
     */
    ResponseResult findAssociate(UserSearchDto userSearchDto);
 
}