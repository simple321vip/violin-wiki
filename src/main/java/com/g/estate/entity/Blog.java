package com.g.estate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blog")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private long id;

    @Column(name = "delete_flg")
    private String deleteFlg;

    @Column(name = "blog_type_id")
    private long typeId;

    @Column(name = "blog_title")
    private String blogTitle;

    @Column(name = "blog_prex")
    private String blogPrex;

    @Column(name = "blog_text_path")
    private String blogTextPath;
}
