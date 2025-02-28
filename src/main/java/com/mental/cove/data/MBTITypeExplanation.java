package com.mental.cove.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MBTITypeExplanation extends ResultExplanation{
    private String mbtiType;
    private String personality;
    private List<Percentage> percentages;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Percentage implements Serializable {
        private String label;
        private Integer percentage;
        private String secondLabel;
    }

}
