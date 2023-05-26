package com.sangeng.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.entity.Article;
import com.sangeng.mapper.ArticleMapper;
import com.sangeng.service.ArticleService;
import org.springframework.stereotype.Service;

@Service
public class ArticeServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService
{
}
