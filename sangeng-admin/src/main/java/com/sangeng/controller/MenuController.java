package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.vo.MenuVo;
import com.sangeng.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController
{

    @Autowired
    private MenuService menuService;

    @GetMapping("/treeselect")
    public ResponseResult treeSelect()
    {
        List<MenuVo> menuVos = menuService.treeSelect();
        return ResponseResult.okResult(menuVos);
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable Long id)
    {
        return menuService.roleMenuTreeselect(id);
    }
}
