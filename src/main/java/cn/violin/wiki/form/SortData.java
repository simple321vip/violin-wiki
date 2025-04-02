package cn.violin.wiki.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SortData {

    @JsonProperty("btIds")
    private String[] btIds;

    @JsonProperty("bIds")
    private String[] bIds;

}
