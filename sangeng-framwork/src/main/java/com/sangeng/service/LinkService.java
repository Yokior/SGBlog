package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Link;
import com.sangeng.domain.vo.LinkVo;
import org.springframework.web.bind.annotation.RestController;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-05-31 13:52:05
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult listLink(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(LinkVo linkVo);
}
