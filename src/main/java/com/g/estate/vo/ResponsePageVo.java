package com.g.estate.vo;


import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePageVo {

    private int page;
    private int total;
    private int pagePer;


    public class ResponsePageVoBuilder{

        private int page;
        private int total;
        private int pagePer;


    }

}
