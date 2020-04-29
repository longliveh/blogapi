package com.bobilwm.weibo.entity.blog;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table
public class Blog {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userid;
    @Column(columnDefinition = "text")
    private String content;
    @Column(columnDefinition = "tinyint")
    private Short mediaType;//0：仅文字    1：图片    2：视频      3:直播
    @Column(columnDefinition = "text")
    private String urlList;//图片地址或者视频地址
    private Date date;
    @Column(columnDefinition = "INT default 0")
    private Integer star=0;
}
