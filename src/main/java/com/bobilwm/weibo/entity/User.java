package com.bobilwm.weibo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@ToString
@Getter
@Setter
@Entity
@Table
public class User implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;
    private String tel;
    private String nickname;
    private Integer sex;
    @Column(unique = true)
    private String email;
    private String password;
    private String salt;
    private String avatar="default_avatar.jpg";
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.REMOVE,CascadeType.REFRESH})
    @JoinTable(
            name = "USER_ROlE",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    private List<Role> roles;

    public String getCredentialsSalt() {
        return salt;
    }

    public User() {
        this.roles = new ArrayList<>();
    }
}