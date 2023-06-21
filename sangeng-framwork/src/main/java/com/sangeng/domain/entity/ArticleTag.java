package com.sangeng.domain.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 文章标签关联表(ArticleTag)表实体类
 *
 * @author makejava
 * @since 2023-06-21 14:36:56
 */
@TableName(value="sg_article_tag")
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTag implements Serializable
{
    private static final long serialVersionUID = 625337492348897098L;

    //文章id
    private Long articleId;
    //标签id
    private Long tagId;

}

