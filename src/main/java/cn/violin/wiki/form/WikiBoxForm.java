package cn.violin.wiki.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WikiBoxForm {

    @JsonProperty("btId")
    private String btId;

    @JsonProperty("btName")
    @NotNull
    private String btName;

    @JsonProperty("order")
    @NotNull
    private int order;
}
