package cn.violin.home.book.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BookmarkTypeIn {

    @JsonProperty("typeId")
    private String typeId;

    @JsonProperty("typeName")
    @NotNull
    private String typeName;
}
