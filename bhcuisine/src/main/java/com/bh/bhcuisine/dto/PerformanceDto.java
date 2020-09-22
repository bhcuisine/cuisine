package com.bh.bhcuisine.dto;

import lombok.Data;

import java.util.List;

/**
 * 存储绩效率dto对象
 */
@Data
public class PerformanceDto {

    /**
     * 绩效率
     */
    private Integer performance;
    /**
     * 存储绩效率id
     */
    private List<Integer> ids;

}
