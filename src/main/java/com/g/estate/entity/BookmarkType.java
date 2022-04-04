package com.g.estate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookmark_type")
public class BookmarkType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bk_type_id")
    private long typeId;

    @Column(name = "bk_type_name")
    private String typeName;
}
