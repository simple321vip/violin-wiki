package cn.violin.wiki.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileForm {

    @JsonProperty("content")
    private String content;

    @JsonProperty("name")
    @Pattern(regexp = "^[A-Za-z0-9]{1,57}$", message = "Profile name 应该在4-16字符之间, 不可以包含特殊字符")
    private String name;

}
