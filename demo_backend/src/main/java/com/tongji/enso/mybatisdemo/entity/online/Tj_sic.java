package com.tongji.enso.mybatisdemo.entity.online;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tj_sic {
    private int id;
    private String year;
    private String month;
    private String day;
    private String var_model;
    private String data;
}
