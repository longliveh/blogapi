package com.bobilwm.weibo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
public class Role implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String role;

    //懒加载 不会查询role表
    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private List<User> users;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private List<Permission> permissions;

    public Role() {
    }

    public Role(String role) {
        this.role = role;
    }

}
