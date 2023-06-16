package com.sangeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.entity.Role;
import com.sangeng.mapper.RoleMapper;
import com.sangeng.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-06-15 15:01:10
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService
{

    @Override
    public List<String> selectRoleKeyByUserId(Long id)
    {
        // 判断是否为管理员 如果是集合只需要返回admin
        if (id == 1L)
        {
            ArrayList<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }

        // 否则查询用户具有的角色信息

        return getBaseMapper().selectRoleKeyByUserId(id);
    }
}
