package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangeng.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-14 14:36:09
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}

