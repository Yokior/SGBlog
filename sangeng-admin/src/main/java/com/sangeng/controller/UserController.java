package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.vo.SysUserVo;
import com.sangeng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController
{

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult listSysUserInfo(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status)
    {
        return userService.listSysUserInfo(pageNum, pageSize, userName, phonenumber, status);
    }

    @PostMapping
    public ResponseResult addSysUser(@RequestBody SysUserVo sysUserVo)
    {
        return userService.addSysUser(sysUserVo);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteSysUser(@PathVariable Long id)
    {
        return userService.deleteSysUser(id);
    }
}
