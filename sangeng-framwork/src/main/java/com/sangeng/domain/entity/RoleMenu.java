package com.sangeng.domain.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 角色和菜单关联表(RoleMenu)表实体类
 *
 * @author makejava
 * @since 2023-06-29 21:54:37
 */
@TableName(value="sys_role_menu")
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenu {
    //角色ID
    private Long roleId;
    //菜单ID
    private Long menuId;

}

