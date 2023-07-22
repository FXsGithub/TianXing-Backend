package com.tongji.enso.mybatisdemo.entity.online;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tj_sie {
    private int id;
    private String year;
    private String month;
    private String var_model;
    private String data;
    private double[] trans_data;
}
