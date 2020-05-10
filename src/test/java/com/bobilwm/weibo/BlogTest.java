package com.bobilwm.weibo;

import com.bobilwm.weibo.entity.blog.Blog;
import com.bobilwm.weibo.entity.blog.Comment;
import com.bobilwm.weibo.entity.blog.LikeTo;
import com.bobilwm.weibo.repository.BlogRepository;
import com.bobilwm.weibo.repository.CommentRepository;
import com.bobilwm.weibo.repository.LikeToRepository;
import com.bobilwm.weibo.service.BlogService;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.jms.*;
import javax.jms.Queue;
import java.util.*;

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


    @Test
    public void aaa()
    {
        int n = 10;
        n |= n>>1;
        System.out.println();
        HashMap hashMap = new HashMap();
        int hm = hashMap.hashCode();
        HashSet hashSet = new HashSet();
        int hs = hashMap.hashCode();
        System.out.println();
    }

    @Test
    public void fasd() throws Exception {
        //1、创建工厂连接对象，需要制定ip和端口号
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin","tcp://47.102.214.2:61616");
        //2、使用连接工厂创建一个连接对象
        Connection connection = connectionFactory.createConnection();
        //3、开启连接
        connection.start();
        //4、使用连接对象创建会话（session）对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、使用会话对象创建目标对象，包含queue和topic（一对一和一对多）
        Queue queue = session.createQueue("springboot.queue");
        //6、使用会话对象创建生产者对象
        MessageConsumer consumer = session.createConsumer(queue);
        //7、向consumer对象中设置一个messageListener对象，用来接收消息
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                // TODO Auto-generated method stub
                if(message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage)message;
                    try {
                        System.out.println(textMessage.getText());
                    } catch (JMSException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        //8、程序等待接收用户消息
        System.in.read();
        //9、关闭资源
        consumer.close();
        session.close();
        connection.close();

    }

}
