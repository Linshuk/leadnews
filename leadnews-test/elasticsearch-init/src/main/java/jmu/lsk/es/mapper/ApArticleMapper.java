package jmu.lsk.es.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jmu.lsk.es.pojo.SearchArticleVo;
import jmu.lsk.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    public List<SearchArticleVo> loadArticleList();

    public SearchArticleVo loadArticle(String title);

}
