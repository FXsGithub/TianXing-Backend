package com.tongji.enso.mybatisdemo.entity.online;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor //全参构造函数
@NoArgsConstructor //无参构造函数
public class info_sic_latlon {
    private int id;
    private String lat;
    private String lon;
}
