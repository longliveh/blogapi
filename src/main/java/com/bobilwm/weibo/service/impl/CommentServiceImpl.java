package com.bobilwm.weibo.service.impl;

import com.bobilwm.weibo.entity.blog.Comment;
import com.bobilwm.weibo.repository.CommentRepository;
import com.bobilwm.weibo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Override
    public List<Comment> getComment(Integer orderby, Integer blogid, Long father,Integer comment_p) {
        if (orderby == 0)//点赞数
        {
            return commentRepository.findAllByBlogIdAndFatherAndIsDeleteOrderByStarDesc(blogid, father,false,comment_p);
        } else {
            return commentRepository.findAllByBlogIdAndFatherAndIsDeleteOrderByDateDesc(blogid, father,false,comment_p);
        }

    }

    @Override
    public Integer getUseridByCom(Long commentid) {
        return commentRepository.findById(commentid).getFromUser();
    }

    @Override
    public List<Comment> getRecentComment(Integer userid) {

        return commentRepository.whoCommentMe(userid);
    }

    @Override
    public Comment getCommentById(Long c_id) {
        return commentRepository.findById(c_id);
    }

    @Override
    public Boolean deleteComment(Long com_id) {
        return commentRepository.deleteComment(com_id)>0;
    }
}

