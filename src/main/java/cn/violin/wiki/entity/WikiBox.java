package cn.violin.wiki.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "T_WIKI_BOX")
@Builder
public class WikiBox {
    @Id
    private String btId;

    @Column(name = "comment")
    private String btName;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "order")
    private int order;
}
