package jmu.lsk.article.controller.v1;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jmu.lsk.article.service.ApArticleService;
import jmu.lsk.file.service.FileStorageService;
import jmu.lsk.model.common.article.dtos.ArticleHomeDto;
import jmu.lsk.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jmu.lsk.common.constants.ArticleConstants;

@RestController
@RequestMapping("/api/v1/article")
@Tag(name = "app文章",description = "app文章列表API")
public class ArticleHomeController {

    @Autowired
    ApArticleService apArticleService;

    @PostMapping("/load")
    @Operation(summary = "文章列表")
    public ResponseResult load(@RequestBody ArticleHomeDto dto) {
        return apArticleService.load(ArticleConstants.LOADTYPE_LOAD_MORE,dto);
    }

    @PostMapping("/loadmore")
    @Operation(summary = "更多文章")
    public ResponseResult loadMore(@RequestBody ArticleHomeDto dto) {
        return apArticleService.load(ArticleConstants.LOADTYPE_LOAD_MORE,dto);
    }

    @PostMapping("/loadnew")
    @Operation(summary = "最新文章")
    public ResponseResult loadNew(@RequestBody ArticleHomeDto dto) {
        return apArticleService.load(ArticleConstants.LOADTYPE_LOAD_NEW,dto);
    }
}