package com.bobilwm.weibo;

import com.bobilwm.weibo.entity.blog.Blog;
import com.bobilwm.weibo.entity.blog.Comment;
import com.bobilwm.weibo.entity.blog.LikeTo;
import com.bobilwm.weibo.repository.BlogRepository;
import com.bobilwm.weibo.repository.CommentRepository;
import com.bobilwm.weibo.repository.LikeToRepository;
import com.bobilwm.weibo.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogTest {
    @Autowired
    BlogService blogService;

    @Autowired
    LikeToRepository likeToRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    BlogRepository blogRepository;

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
//        List l = commentRepository.findAllByBlogIdAndFatherOrderByStarDesc(1, (long) -1);
        System.out.println();
    }

    @Test
    void who()
    {
        List<Comment> list= commentRepository.whoCommentMe(1);
        System.out.println();
    }

    @Test
    void wholikedme()
    {
        List<LikeTo> likeTos = likeToRepository.getWhoLikedMe(1);
        System.out.println();

    }

    @Test
    void getblog0()
    {

//        List list = blogService.getBlog(0,1);
        System.out.println();
    }

    @Test
    public void restTemplateGetTest(){
        try {
            RestTemplate restTemplate = new RestTemplate();
            //将指定的url返回的参数自动封装到自定义好的对应类对象中
            ResponseEntity<String> re = restTemplate.getForObject("http://47.102.214.2/hls/1/index.m3u8",ResponseEntity.class);
            System.out.println(re);
        }catch (HttpClientErrorException e){
            if (e.getRawStatusCode()==404)
            {
                System.out.println("http客户端请求出错了！");
            }
            //开发中可以使用统一异常处理，或者在业务逻辑的catch中作响应
        }catch (RestClientException e)
        {
            System.out.println("http客户端请求出错了！");
        }
    }



}
