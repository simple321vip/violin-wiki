package cn.violin.home.book.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BlogTypeIn {

    @JsonProperty("btId")
    private String btId;

    @JsonProperty("btName")
    @NotNull
    private String btName;
}
