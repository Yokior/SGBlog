package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.vo.AddArticleDto;
import com.sangeng.domain.vo.ArticleInfoDto;

public interface ArticleService extends IService<Article>
{
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);

    ResponseResult listArticle(int pageNum, int pageSize, String title, String summary);

    ResponseResult getArticleInfo(Long id);
}
