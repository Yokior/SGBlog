package com.sangeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.vo.HotArticleVo;
import com.sangeng.mapper.ArticleMapper;
import com.sangeng.service.ArticleService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class ArticeServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService
{
    @Override
    public ResponseResult hotArticleList()
    {
        // 查询热门文章
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        // 必须是正式文章 不能是草稿或者删除的文章
        lqw.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 按照浏览量进行排序
        lqw.orderByDesc(Article::getViewCount);
        // 最多查询10条文章
        Page<Article> articlePage = new Page<>(1, 10);
        page(articlePage, lqw);
        List<Article> articles = articlePage.getRecords();

        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }
}
