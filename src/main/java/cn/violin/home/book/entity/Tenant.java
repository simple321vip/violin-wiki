package cn.violin.home.book.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "t_tenant")
@Builder
public class Tenant {

    private String id;

    private String account;

    private String tel;

    private int authority;

    private String avatarUrl;

    private String storageAccount;
}
