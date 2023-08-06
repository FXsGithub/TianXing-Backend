package com.tongji.enso.mybatisdemo.entity.online;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Info_sic_latlon {
    private int id;
    private String lat;//纬度
    private String lon;//经度
}
