package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.entity.Category;
import com.sangeng.domain.vo.ArticleDetailVo;
import com.sangeng.domain.vo.ArticleListVo;
import com.sangeng.domain.vo.HotArticleVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.mapper.ArticleMapper;
import com.sangeng.service.ArticleService;
import com.sangeng.service.CategoryService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class ArticeServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService
{
    @Lazy
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

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

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId)
    {
        // 条件查询
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();

        // 如果有categoryId 分类排序
        lqw.eq(categoryId != null && categoryId > 0,Article::getCategoryId,categoryId);
        // 查询文章是已发布
        lqw.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 置顶文章
        lqw.orderByDesc(Article::getIsTop);

        // 分页查询
        Page<Article> articlePage = new Page<>(pageNum,pageSize);
        page(articlePage,lqw);

        List<Article> articleList = articlePage.getRecords();
        // 补充数据 categoryName
        articleList.stream()
                .forEach(article ->
                {
                    Category category = categoryService.getById(article.getCategoryId());
                    article.setCategoryName(category.getName());
                });


        // 封装成vo
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articlePage.getRecords(), ArticleListVo.class);


        PageVo pageVo = new PageVo(articleListVos, articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id)
    {
        // 根据ID获取文章
        Article article = getById(id);
        // 从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, String.valueOf(id));
        article.setViewCount(viewCount.longValue());

        // 补充数据 分类名称
        Category category = categoryService.getById(article.getCategoryId());
        if (category != null)
        {
            article.setCategoryName(category.getName());
        }

        // 封装成vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);

        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id)
    {
        // 更新redis中对应id的浏览量
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT,id.toString(),1);

        return ResponseResult.okResult();
    }
}
