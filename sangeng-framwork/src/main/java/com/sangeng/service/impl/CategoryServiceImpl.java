package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.entity.Category;
import com.sangeng.domain.vo.CategoryVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.mapper.CategoryMapper;
import com.sangeng.service.ArticleService;
import com.sangeng.service.CategoryService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-05-28 14:34:24
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService
{
    @Autowired
    private ArticleService articleService;

    // 获取分类列表
    @Override
    public ResponseResult getCategoryList()
    {
        // 查询文章表状态为已发布的
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(lqw);

        // 获取文章的分类ID并去重
        List<Long> categoryIdList = articleList.stream()
                .map(Article::getCategoryId)
                .distinct()
                .collect(Collectors.toList());

        // 查询分类表
        List<Category> categories = listByIds(categoryIdList);
        categories = categories.stream()
                .filter(category ->
                {
                    return category.getStatus().equals(SystemConstants.STATUS_NORMAL);
                })
                .collect(Collectors.toList());

        // 封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory()
    {
        // 查询所有分类信息
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        List<Category> categoryList = list(lqw);

        // 封装vo返回
        List<CategoryVo> categoryVoList = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);

        return ResponseResult.okResult(categoryVoList);
    }

    @Override
    public ResponseResult listCategory(Integer pageNum, Integer pageSize, String name, String status)
    {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.select(Category::getId, Category::getDescription, Category::getStatus, Category::getName);
        // 根据name模糊查询
        lqw.like(StringUtils.hasText(name), Category::getName, name);
        // 根据status查询
        lqw.eq(StringUtils.hasText(status), Category::getStatus, status);

        // 封装page
        Page<Category> categoryPage = new Page<>(pageNum, pageSize);
        page(categoryPage, lqw);

        // 封装PageVo
        PageVo pageVo = new PageVo(categoryPage.getRecords(), categoryPage.getTotal());

        return ResponseResult.okResult(pageVo);
    }
}

