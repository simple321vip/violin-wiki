package cn.violin.wiki.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_PROFILE")
@Builder
public class Profile {
    @Id
    @Column(name = "tenant_id")
    private String tenantId;

    private String content;
    private String name;

    @Column(name = "update_time")
    private String updateTime;
}
