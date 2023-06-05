package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Comment;
import com.sangeng.domain.vo.CommentVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.mapper.CommentMapper;
import com.sangeng.service.CommentService;
import com.sangeng.service.UserService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-06-04 15:14:31
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService
{

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize)
    {
        // 查询对应文章的根评论


        // 对articleId进行判断
        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();

        // 根评论 rootId为-1
        lqw.eq(Comment::getArticleId, articleId);
        lqw.eq(Comment::getRootId, -1);

        // 分页查询
        Page<Comment> commentPage = new Page<>(pageNum, pageSize);
        page(commentPage, lqw);

        List<CommentVo> commentVos = toCommentVoList(commentPage.getRecords());

        // 查询所有根评论对应的子评论并赋值对应属性
        commentVos.stream()
                .forEach(commentVo ->
                {
                    List<CommentVo> children = getChildren(commentVo.getId());
                    commentVo.setChildren(children);
                });


        return ResponseResult.okResult(new PageVo(commentVos, commentPage.getTotal()));
    }

    /**
     * 获取子评论
     * @param id
     * @return
     */
    private List<CommentVo> getChildren(Long id)
    {
        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Comment::getRootId,id);
        lqw.orderByAsc(Comment::getCreateTime);
        List<Comment> commentList = list(lqw);

        List<CommentVo> commentVoList = toCommentVoList(commentList);
        return commentVoList;
    }

    /**
     * 转换vo
     * @param list
     * @return
     */
    private List<CommentVo> toCommentVoList(List<Comment> list)
    {
        List<CommentVo> commentVoList = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        // 遍历vo集合

        // 通过createBy查询用户
        commentVoList.stream()
                .forEach(commentVo ->
                {
                    String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
                    commentVo.setUsername(nickName);
                });

        // 通过toCommentUserId查询用户名
        commentVoList.stream()
                .filter(commentVo ->
                {
                    if (commentVo.getToCommentUserId() != -1)
                    {
                        return true;
                    }
                    return false;
                })
                .forEach(commentVo ->
                {
                    String nickName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                    commentVo.setToCommentUserName(nickName);
                });

        return commentVoList;
    }
}

