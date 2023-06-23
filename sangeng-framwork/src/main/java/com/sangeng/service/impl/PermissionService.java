package com.sangeng.service.impl;

import com.sangeng.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService
{
    /**
     * 判断当前用户是否具有permission权限
     * @param perm
     * @return
     */
    public boolean hasPermission(String perm)
    {
        // 如果是超级管理员 直接返回true
        if (SecurityUtils.isAdmin())
        {
            return true;
        }

        // 否则 查询当前用户具有的权限列表 判断
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(perm);
    }
}
