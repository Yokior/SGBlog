package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Link;
import com.sangeng.domain.vo.LinkVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.mapper.LinkMapper;
import com.sangeng.service.LinkService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-05-31 13:52:05
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService
{

    @Override
    public ResponseResult getAllLink()
    {
        // 查询所有审核通过的链接
        LambdaQueryWrapper<Link> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> linkList = list(lqw);
        // 封装vo
        List<LinkVo> linkVoList = BeanCopyUtils.copyBeanList(linkList, LinkVo.class);

        return ResponseResult.okResult(linkList);
    }

    @Override
    public ResponseResult listLink(Integer pageNum, Integer pageSize, String name, String status)
    {
        LambdaQueryWrapper<Link> lqw = new LambdaQueryWrapper<>();
        // 根据名称模糊查询
        lqw.like(StringUtils.hasText(name),Link::getName,name);
        // 根据状态查询
        lqw.eq(StringUtils.hasText(status),Link::getStatus,status);
        // 分页处理
        Page<Link> linkPage = new Page<>(pageNum, pageSize);
        page(linkPage,lqw);
        // 封装返回
        List<LinkVo> linkVoList = BeanCopyUtils.copyBeanList(linkPage.getRecords(), LinkVo.class);

        PageVo pageVo = new PageVo(linkVoList, linkPage.getTotal());

        return ResponseResult.okResult(pageVo);
    }
}

