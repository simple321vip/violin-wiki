package cn.violin.home.book.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "g_user")
public class User {
    @Id
    @Column(name = "user_id")
    private String id;

    @Column(name = "user_name")
    private String username;

    @Column(name = "user_password")
    private String password;
}
