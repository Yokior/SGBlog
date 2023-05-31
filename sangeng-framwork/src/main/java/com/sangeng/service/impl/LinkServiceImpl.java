package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Link;
import com.sangeng.domain.vo.LinkVo;
import com.sangeng.mapper.LinkMapper;
import com.sangeng.service.LinkService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

