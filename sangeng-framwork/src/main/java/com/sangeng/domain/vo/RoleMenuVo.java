package com.sangeng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuVo
{
    // 菜单树
    private List<MenuVo> menus;

    // 角色所关联的菜单权限id列表
    private List<Long> checkedKeys;
}
