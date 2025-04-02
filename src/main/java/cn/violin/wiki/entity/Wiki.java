package cn.violin.wiki.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "T_WIKI")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wiki implements Serializable {
    @Id
    private String bid;

    @Column(name = "type_id")
    private String btId;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "title")
    private String title;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "content")
    private String content;

    private long size;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "order")
    private int order;
}