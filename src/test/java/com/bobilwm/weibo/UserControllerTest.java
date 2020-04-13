package com.bobilwm.weibo;

import com.bobilwm.weibo.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,classes = WeiboApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {
 @Autowired
 private MockMvc mockMvc;

 @Test
 @DisplayName("测试controller方法")
 void test() throws Exception {

  MvcResult mvcResult= mockMvc.perform(MockMvcRequestBuilders.post("/login")
          .content("{'username':'1375070813@qq.com','password':'123456'}")
          .accept(MediaType.TEXT_HTML_VALUE))
          // .andExpect(MockMvcResultMatchers.status().isOk())             //等同于Assert.assertEquals(200,status);
          // .andExpect(MockMvcResultMatchers.content().string("hello lvgang"))    //等同于 Assert.assertEquals("hello lvgang",content);
          .andDo(MockMvcResultHandlers.print())
          .andReturn();
 }
}