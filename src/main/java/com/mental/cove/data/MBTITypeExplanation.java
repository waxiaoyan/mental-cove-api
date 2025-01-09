package com.mental.cove.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class MBTITypeExplanation {
    private String type;
    private String personality;
    private List<Percentage> percentages;
    //add a inner class which include label percentage and secondLabel
    @Data
    @AllArgsConstructor
    public static class Percentage {
        private String label;
        private Integer percentage;
        private String secondLabel;
    }

}
