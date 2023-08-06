package com.tongji.enso.mybatisdemo.entity.online;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tj_nao {
    private int id;
    private String year;
    private String month;
    private String data;
    private String var_model;
}
