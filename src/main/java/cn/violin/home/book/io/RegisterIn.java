package cn.violin.home.book.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RegisterIn {

    @JsonProperty("phone_number")
    @NotNull
    private String phoneNumber;

    @JsonProperty("code")
    @NotNull
    private String code;

    @JsonProperty("tenant_id")
    @NotNull
    private String tenantId;

    @JsonProperty("account")
    @NotNull
    private String account;

    @JsonProperty("token")
    @NotNull
    private String token;
}