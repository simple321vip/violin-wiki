package cn.violin.home.book.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "t_bookmark")
public class Bookmark {
    @Id
    private String id;
    private String deleteFlg;
    private String typeId;
    private String comment;
    private String url;
}
