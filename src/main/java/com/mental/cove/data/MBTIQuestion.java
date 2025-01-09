package com.mental.cove.data;

import lombok.Data;

@Data
public class MBTIQuestion {
    private int id;
    private String question;
    private String a1;
    private String a2;
    private String a1_type;
    private String a2_type;
    private int a1_Count;
    private int a2_Count;
}
