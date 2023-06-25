package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.entity.ArticleTag;
import com.sangeng.domain.entity.Category;
import com.sangeng.domain.vo.*;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.mapper.ArticleMapper;
import com.sangeng.service.ArticleService;
import com.sangeng.service.ArticleTagService;
import com.sangeng.service.CategoryService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class ArticeServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService
{
    @Lazy
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

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
        lqw.eq(categoryId != null && categoryId > 0, Article::getCategoryId, categoryId);
        // 查询文章是已发布
        lqw.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 置顶文章
        lqw.orderByDesc(Article::getIsTop);

        // 分页查询
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        page(articlePage, lqw);

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
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, id.toString(), 1);

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto)
    {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);


        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listArticle(int pageNum, int pageSize, String title, String summary)
    {
        // 根据title summary模糊查询
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.hasText(title),Article::getTitle,title);
        lqw.like(StringUtils.hasText(summary),Article::getSummary,summary);
        lqw.eq(Article::getStatus,SystemConstants.STATUS_NORMAL);

        // 封装page
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        page(articlePage,lqw);

        // 转换vo
        PageVo pageVo = new PageVo(articlePage.getRecords(), articlePage.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleInfo(Long id)
    {
        // 根据id查询Article
        Article article = getById(id);

        if(article == null)
        {
            return ResponseResult.errorResult(AppHttpCodeEnum.ARTICLE_NOT_EXIST);
        }

        // 查询tag 封装
        LambdaQueryWrapper<ArticleTag> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleTag::getArticleId,id);
        lqw.select(ArticleTag::getTagId);
        List<ArticleTag> articleTagList = articleTagService.list(lqw);

        // 提取tagId
        List<Long> tagIdList = articleTagList.stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toList());

        ArticleInfoDto articleInfoDto = BeanCopyUtils.copyBean(article, ArticleInfoDto.class);
        articleInfoDto.setTags(tagIdList);

        return ResponseResult.okResult(articleInfoDto);
    }
}
