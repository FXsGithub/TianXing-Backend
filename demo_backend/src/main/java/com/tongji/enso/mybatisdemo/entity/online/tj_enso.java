package com.tongji.enso.mybatisdemo.entity.online;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor //全参构造函数
@NoArgsConstructor //无参构造函数
public class tj_enso {
    private int id;
    private String year;
    private String month;
    private String var_model;
    private String data;
}

