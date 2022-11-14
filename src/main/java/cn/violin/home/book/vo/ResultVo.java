package cn.violin.home.book.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultVo {

    @JsonProperty("process_status")
    private boolean processStatus;

}
