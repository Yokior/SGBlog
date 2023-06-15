package com.sangeng.constants;

public class SystemConstants
{
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常分布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    public static final String STATUS_NORMAL = "0";

    /**
     * 友链审核通过
     */
    public static final String LINK_STATUS_NORMAL = "0";
    public static final String ARTICLE_COMMENT = "0";
    public static final String LINK_COMMENT = "1";

    // redis中的key
    public static final String ARTICLE_VIEW_COUNT = "article:viewCount";


    public static final String MENU = "C";
    public static final String BUTTON = "F";
}