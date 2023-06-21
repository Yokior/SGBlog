package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.vo.TagInfoVo;
import com.sangeng.domain.vo.TagListDto;
import com.sangeng.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController
{

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto)
    {
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @PostMapping
    public ResponseResult add(@RequestBody TagListDto tagListDto)
    {
        return tagService.add(tagListDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable Long id)
    {
        return tagService.deleteTag(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getTagInfo(@PathVariable Long id)
    {
        return tagService.getTagInfo(id);
    }

    @PutMapping
    public ResponseResult setTagInfo(@RequestBody TagInfoVo tagInfoVo)
    {
        return tagService.setTagInfo(tagInfoVo);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag()
    {
        return tagService.listAllTag();
    }
}
