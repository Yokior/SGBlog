package com.sangeng.runner;

import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.entity.Article;
import com.sangeng.mapper.ArticleMapper;
import com.sangeng.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class ViewCountRunner implements CommandLineRunner
{

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception
    {
        // 查询博客信息 id viewCount
        List<Article> articleList = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articleList.stream()
                .collect(Collectors.toMap(article -> String.valueOf(article.getId()), article -> article.getViewCount().intValue()));

        // 存入redis
        redisCache.setCacheMap(SystemConstants.ARTICLE_VIEW_COUNT,viewCountMap);
    }
}
