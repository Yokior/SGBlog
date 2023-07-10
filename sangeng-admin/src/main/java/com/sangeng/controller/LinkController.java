package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.vo.LinkVo;
import com.sangeng.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController
{

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult listLink(Integer pageNum, Integer pageSize, String name, String status)
    {
        return linkService.listLink(pageNum, pageSize, name, status);
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody LinkVo linkVo)
    {
        return linkService.addLink(linkVo);
    }

    @GetMapping("/{id}")
    public ResponseResult getLink(@PathVariable Long id)
    {
        return linkService.getLink(id);
    }

    @PutMapping
    public ResponseResult updateLink(@RequestBody LinkVo linkVo)
    {
        return linkService.updateLink(linkVo);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable Long id)
    {
        return linkService.deleteLink(id);
    }

}
