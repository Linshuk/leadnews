package jmu.lsk.wemedia.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jmu.lsk.model.common.wemedia.pojos.WmUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WmUserMapper extends BaseMapper<WmUser> {
}