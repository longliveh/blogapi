package com.bobilwm.weibo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
public class Permission implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}