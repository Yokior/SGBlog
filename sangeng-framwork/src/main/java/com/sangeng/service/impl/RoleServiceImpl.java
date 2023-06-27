package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Role;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.RoleVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.mapper.RoleMapper;
import com.sangeng.service.RoleService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    public ResponseResult listRoles(int pageNum, int pageSize, String roleName, String status)
    {
        // 根据roleName status查询role
        LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<>();
        lqw.like(roleName != null, Role::getRoleName, roleName);
        lqw.eq(status != null, Role::getStatus, status);

        // 根据roleSort进行升序排列
        lqw.orderByAsc(Role::getRoleSort);

        // 分页
        Page<Role> rolePage = new Page<>(pageNum, pageSize);
        page(rolePage,lqw);

        if (rolePage.getTotal() <= 0)
        {
            return ResponseResult.errorResult(AppHttpCodeEnum.ROLE_NOT_EXIST);
        }

        // 转换为vo
        List<RoleVo> roleVoList = BeanCopyUtils.copyBeanList(rolePage.getRecords(), RoleVo.class);

        // 转换为PageVo
        PageVo pageVo = new PageVo(roleVoList, rolePage.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(Map<String,String> roleMap)
    {
        // 根据角色Id获取角色
        Role role = getById(roleMap.get("roleId"));
        if (role == null)
        {
            return ResponseResult.errorResult(AppHttpCodeEnum.ROLE_NOT_EXIST);
        }

        // 修改角色信息
        role.setStatus(roleMap.get("status"));

        updateById(role);
        return ResponseResult.okResult();
    }
}

