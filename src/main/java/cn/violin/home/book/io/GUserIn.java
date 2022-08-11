package cn.violin.home.book.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GUserIn {

    @JsonProperty("user_id")
    @NotNull
    private String userId;

    @JsonProperty("user_password")
    @NotNull
    private String password;
}
