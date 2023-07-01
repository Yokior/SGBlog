package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.vo.RoleInfoVo;
import com.sangeng.domain.vo.RoleVo;
import com.sangeng.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/system/role")
public class RoleController
{

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult listRoles(int pageNum, int pageSize, String roleName, String status)
    {
        return roleService.listRoles(pageNum, pageSize, roleName, status);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody Map<String, String> roleMap)
    {
        return roleService.changeStatus(roleMap);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody RoleVo roleVo)
    {
        return roleService.addRole(roleVo);
    }

    @GetMapping("/{id}")
    public ResponseResult getRoleInfo(@PathVariable Long id)
    {
        return roleService.getRoleInfo(id);
    }

    @PutMapping
    public ResponseResult updateRoleInfo(@RequestBody RoleVo roleVo)
    {
        return roleService.updateRoleInfo(roleVo);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable Long id)
    {
        return roleService.deleteRole(id);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole()
    {
        return roleService.listAllRole();
    }
}
