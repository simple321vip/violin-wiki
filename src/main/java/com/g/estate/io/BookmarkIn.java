package com.g.estate.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkIn {

    @JsonProperty("bk_id")
    private long id;

    @JsonProperty("bk_type_id")
    private long typeId;

    @JsonProperty("url")
    private String url;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("delete_flg")
    private String deleteFlg;
}
