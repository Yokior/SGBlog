package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Tag;
import com.sangeng.domain.vo.TagListDto;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-06-14 14:36:09
 */
public interface TagService extends IService<Tag> {

    ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);
}
