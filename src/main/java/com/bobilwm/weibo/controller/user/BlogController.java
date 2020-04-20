package com.bobilwm.weibo.controller.user;

import com.bobilwm.weibo.controller.respmsg.Result;
import com.bobilwm.weibo.controller.respmsg.ResultCode;
import com.bobilwm.weibo.entity.User;
import com.bobilwm.weibo.entity.blog.Blog;
import com.bobilwm.weibo.entity.blog.Comment;
import com.bobilwm.weibo.service.BlogService;
import com.bobilwm.weibo.utils.FileUtil;
import javafx.scene.layout.VBox;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping
public class BlogController {

    @Autowired
    private BlogService blogService;


    @Value("${file.address}")
    private String uploadPath;

    @PostMapping(value = "/likeBlog")
    public Result likeBlog(@RequestBody JSONObject json) {
        Integer blogid = json.getInt("blogid");
        Boolean isliked = json.getBoolean("isliked");
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        if (isliked==false)
        {
            if (blogService.likeBlogOrComment(user.getId(),blogid,0))
                return Result.success();
            else return Result.error(ResultCode.ERROR);
        }else if (isliked==true)
        {
            if (blogService.unlikeBlogOrComment(user.getId(),blogid,0))
                return Result.success();
            else return Result.error(ResultCode.ERROR);
        }
        return Result.error(ResultCode.ERROR);
    }

    @PostMapping(value = "/getCount")
    public Result getCount(@RequestBody JSONObject json) {
        Integer blogid = json.getInt("blogid");
        Map map = blogService.getBlogDetail(blogid);
        return Result.success(map);
    }

    @PostMapping(value = "/getblograndom")
    public Result getBlogRandom() {
        List<Blog> blogs = blogService.getBlogByRandom();
        return Result.success(blogs);
    }

    @PostMapping(value = "/get_blog_by_userid")
    public Result getBlog(@RequestBody JSONObject json) {
        Integer userid = (Integer) json.get("userid");
        Integer[] userids = {userid};
        List<Blog> blogs = blogService.getBlogByUserid(userids);
        return Result.success(blogs);
    }

    @PostMapping(value = "/get_comment")
    public Result getComment() {
        return Result.error(ResultCode.ERROR);
    }

    @PostMapping(value = "/comment")
    public Result comment(@RequestBody Comment comment) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        comment.setFromUser(user.getId());
        comment.setDate(new Date());
        Long comment_id = blogService.addComment(comment);
        if (comment_id > 0) {
            HashMap map = new HashMap<String, Long>();
            map.put("comment_id", comment_id);
            return Result.success(map);
        }
        return Result.error(ResultCode.ERROR);
    }


    @PostMapping(value = "/publish")
    public Result publish(@RequestParam(value = "filelist") List<MultipartFile> filelist, @RequestParam(value = "content") String content) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        try {
            //上传目录地址
            String uploadDir = uploadPath + "img/";

            //如果目录不存在，自动创建文件夹
            File dir = new File(uploadDir);

            if (!dir.exists()) {
                dir.mkdir();
            }
            List<String> filepaths = new ArrayList<>();

            //遍历文件数组执行上传
            for (int i = 0; i < filelist.size(); i++) {
                if (filelist.get(i) != null) {
                    //调用上传方法
                    String filename = FileUtil.executeUpload(uploadDir, filelist.get(i));
                    filepaths.add(filename);
                }
            }
            Blog blog = new Blog();
            blog.setContent(content);
            blog.setUrlList(filepaths.toString());
            blog.setUserid(user.getId());
            blog.setMediaType((short) 0);
            if (filelist.size() > 0) {
                blog.setMediaType((short) 1);
            }
            blog.setDate(new Date());
            if (blogService.addBlog(blog)) {
                return Result.success();
            }
        } catch (Exception e) {
            //打印错误堆栈信息
            e.printStackTrace();
            return Result.error(ResultCode.SYSTEM_INNER_ERROR);
        }
        System.out.println();
        return Result.error(ResultCode.ERROR);
    }





}
