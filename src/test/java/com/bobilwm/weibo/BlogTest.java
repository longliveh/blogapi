package com.bobilwm.weibo;

import com.bobilwm.weibo.entity.blog.Blog;
import com.bobilwm.weibo.repository.CommentRepository;
import com.bobilwm.weibo.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BlogTest {
    @Autowired
    BlogService blogService;

    @Autowired
    CommentRepository commentRepository;

    @Test
    void getblog()
    {
        Integer[] userids = {1};
        List<Blog> b =  blogService.getBlogByUserid(userids);
        System.out.println(b);
    }

    @Test
    void getComment()
    {
        List l = commentRepository.findAllByBlogIdAndFatherOrderByStarDesc(1, (long) -1);
        System.out.println();
    }

}
