package com.g.estate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_blog_type")
public class BlogType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_type_id")
    private String typeId;

    @Column(name = "blog_type_name")
    private String typeName;

    @Column(name = "updater_id")
    private String updaterId;

    @Column(name = "update_time")
    private String updateTime;
}
