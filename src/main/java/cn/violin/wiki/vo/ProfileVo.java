package cn.violin.wiki.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileVo {

    @JsonProperty("content")
    private String content;

    @JsonProperty("name")
    private String name;

    @JsonProperty("updateDatetime")
    private String updateDatetime;

}
