package com.g.estate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_bookmark")
public class Bookmark {
    @Id
    @Column(name = "bk_id")
    private String id;

    @Column(name = "delete_flg")
    private String deleteFlg;

    @Column(name = "bk_type_id")
    private long typeId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "url")
    private String url;
}
