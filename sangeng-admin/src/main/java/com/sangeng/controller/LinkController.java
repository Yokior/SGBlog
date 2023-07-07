package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
