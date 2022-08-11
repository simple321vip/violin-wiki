package cn.violin.home.book.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "t_blog_type")
public class BlogType {
    @Id
    private String btId;
    private String btName;
    private String owner;
    private String updateTime;
}
