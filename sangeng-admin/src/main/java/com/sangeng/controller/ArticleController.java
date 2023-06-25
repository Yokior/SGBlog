package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.vo.AddArticleDto;
import com.sangeng.domain.vo.ArticleInfoDto;
import com.sangeng.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController
{

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article)
    {
        return articleService.add(article);
    }

    /**
     * 分页模糊查询文章列表
     * @param pageNum
     * @param pageSize
     * @param title
     * @param summary
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listArticle(int pageNum, int pageSize,String title, String summary)
    {
        return articleService.listArticle(pageNum,pageSize,title,summary);
    }


    @GetMapping("/{id}")
    public ResponseResult getArticleInfo(@PathVariable Long id)
    {
        return articleService.getArticleInfo(id);
    }


    @PutMapping
    public ResponseResult updateArticle(@RequestBody ArticleInfoDto articleInfoDto)
    {
        return articleService.updateArticle(articleInfoDto);
    }


}