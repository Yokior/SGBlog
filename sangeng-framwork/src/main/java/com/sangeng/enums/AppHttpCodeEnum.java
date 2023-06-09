package com.sangeng.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"), EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    CONTENT_NOT_NULL(506,"评论内容不能为空"),
    FILE_TYPE_ERROR(507,"文件类型错误，请上传png"),
    USERNAME_NOT_NULL(508,"用户名不能为空"),
    PASSWORD_NOT_NULL(509,"密码不能为空"),
    EMAIL_NOT_NULL(510,"邮箱不能为空"),
    NICKNAME_NOT_NULL(511,"昵称不能为空"),
    NICKNAME_EXIST(512,"昵称已存在"),
    TAG_NOT_EXIST(513,"标签不存在"),
    ARTICLE_NOT_EXIST(514,"文章不存在"),
    MENU_NOT_EXIST(515,"菜单不存在"),
    MENU_HAS_SON(516,"菜单含有子菜单，删除失败"),
    ROLE_NOT_EXIST(517,"角色不存在"),
    USER_NOT_EXIST(518,"用户不存在"),
    USER_DELETE_ERROR(519,"不可以删除当前操作的用户"),
    CATEGORY_NOT_EXIST(520,"分类不存在"),
    LINK_NOT_EXIST(521,"友链不存在");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
