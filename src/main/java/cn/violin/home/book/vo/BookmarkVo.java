package cn.violin.home.book.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkVo {

    @JsonProperty("bk_id")
    private String id;

    @JsonProperty("bk_type_id")
    private long typeId;

    @JsonProperty("bk_type_name")
    private String typeName;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("url")
    private String url;

}
