package com.g.estate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_blog")
public class Blog {

    @Id
    @Column(name = "blog_id")
    private String id;

    @Column(name = "delete_flg")
    private String deleteFlg;

    @Column(name = "blog_type_id")
    private String typeId;

    @Column(name = "blog_title")
    private String blogTitle;

    @Column(name = "blog_prex")
    private String blogPrex;

    @Column(name = "blog_text_path")
    private String blogTextPath;
}
