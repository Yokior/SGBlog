package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Tag;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.TagInfoVo;
import com.sangeng.domain.vo.TagListDto;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.mapper.TagMapper;
import com.sangeng.service.TagService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-06-14 14:36:09
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService
{

    @Override
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto)
    {
        // 分页查询
        Page<Tag> tagPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Tag> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        lqw.like(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());

        page(tagPage,lqw);

        // 封装数据
        PageVo pageVo = new PageVo(tagPage.getRecords(), tagPage.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult add(TagListDto tagListDto)
    {
        // 创建tag 添加数据
        Tag tag = new Tag();
        tag.setName(tagListDto.getName());
        tag.setRemark(tagListDto.getRemark());

        // 封装
        save(tag);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id)
    {
        // 根据id逻辑删除标签
        removeById(id);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTagInfo(Long id)
    {
        // 根据id获取Tag
        Tag tag = getById(id);
        if (tag == null)
        {
            return ResponseResult.errorResult(AppHttpCodeEnum.TAG_NOT_EXIST);
        }

        // 封装转换vo数据
        TagInfoVo tagInfoVo = BeanCopyUtils.copyBean(tag, TagInfoVo.class);

        return ResponseResult.okResult(tagInfoVo);
    }

    @Override
    public ResponseResult setTagInfo(TagInfoVo tagInfoVo)
    {
        // 根据ID获取原始数据
        Tag tag = getById(tagInfoVo.getId());
        if (tag == null)
        {
            return ResponseResult.errorResult(AppHttpCodeEnum.TAG_NOT_EXIST);
        }

        // 更新指定字段
        tag.setName(tagInfoVo.getName());
        tag.setRemark(tagInfoVo.getRemark());

        // 更新
        updateById(tag);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag()
    {
        // 获取所有tag
        List<Tag> tagList = list();

        // 封装vo返回
        List<TagInfoVo> tagInfoVoList = BeanCopyUtils.copyBeanList(tagList, TagInfoVo.class);

        return ResponseResult.okResult(tagInfoVoList);
    }
}

