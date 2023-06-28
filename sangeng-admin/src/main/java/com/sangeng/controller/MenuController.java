package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/menu")
public class MenuController
{

    @Autowired
    private MenuService menuService;

    @GetMapping("/treeselect")
    public ResponseResult treeSelect()
    {
        return menuService.treeSelect();
    }
}
