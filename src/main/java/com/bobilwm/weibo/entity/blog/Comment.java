package com.bobilwm.weibo.entity.blog;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table
public class Comment {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer blogId;
    private Date date;
    private String content;
    private Long father;//评论的父级，一级评论的父级是-1，二级评论的父级是某一条评论的id
    private Integer fromUser;
    private Long toComment;
    @Column(columnDefinition = "INT default 0")
    private Integer star=0;

    private Boolean isDelete = false;
}
