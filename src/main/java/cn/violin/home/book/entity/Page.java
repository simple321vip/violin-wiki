package cn.violin.home.book.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "page")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_id")
    private long pageId;

    @Column(name = "section_id")
    private long sectionId;

    @Column(name = "page_name")
    private String pageName;

    @Column(name = "page_path")
    private String pagePath;
}
