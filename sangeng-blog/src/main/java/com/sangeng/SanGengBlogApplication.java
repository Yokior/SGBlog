package com.sangeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SanGengBlogApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SanGengBlogApplication.class,args);
    }
}
