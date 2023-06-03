package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.User;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.service.BlogLoginService;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController
{
    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user)
    {
        if (!Strings.hasText(user.getUserName()))
        {
            // 提示 需要传入用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }

        return blogLoginService.login(user);
    }


    @PostMapping("/logout")
    public ResponseResult logout()
    {
        return blogLoginService.logout();
    }
}
