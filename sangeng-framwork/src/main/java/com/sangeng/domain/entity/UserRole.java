package com.sangeng.domain.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户和角色关联表(UserRole)表实体类
 *
 * @author makejava
 * @since 2023-07-01 11:54:36
 */
@TableName(value="sys_user_role")
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    //用户ID
    private Long userId;
    //角色ID
    private Long roleId;

}

