package cn.violin.wiki.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "t_profile")
@Builder
public class Profile {
    @Id
    private String owner;
    private Binary content;
    private String name;
    private String updateDateTime;
}
