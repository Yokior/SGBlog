package com.sangeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Article;
import com.sangeng.mapper.ArticleMapper;
import com.sangeng.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticeServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService
{
    @Override
    public ResponseResult hotArticleList()
    {
        // 查询热门文章
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        // 必须是正式文章 不能是草稿或者删除的文章
        lqw.eq(Article::getStatus,"0");
        // 按照浏览量进行排序
        lqw.orderByDesc(Article::getViewCount);
        // 最多查询10条文章
        Page<Article> articlePage = new Page<>(1,10);
        page(articlePage,lqw);
        List<Article> records = articlePage.getRecords();

        return ResponseResult.okResult(records);
    }
}
