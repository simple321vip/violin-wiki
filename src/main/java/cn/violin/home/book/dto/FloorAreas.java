package cn.violin.home.book.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class FloorAreas {

    private String building;

    private String floor;

    private BigDecimal areas;

}
