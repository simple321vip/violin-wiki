package cn.violin.home.book.entity;

import javax.persistence.Column;

public class BaseEntity {

    @Column(name = "updater_id")
    private String updaterId;

    @Column(name = "update_time")
    private String updateTime;
}
