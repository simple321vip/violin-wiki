package cn.violin.home.book.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkIn {

    @JsonProperty("bk_id")
    private String id;

    @JsonProperty("bk_type_id")
    private String typeId;

    @JsonProperty("url")
    private String url;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("delete_flg")
    private String deleteFlg;

    @JsonProperty("bookmark_type_name")
    private String typeName;
}
