package cn.violin.home.book.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "t_bookmark_type")
public class BookmarkType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long typeId;
    private String typeName;
}
