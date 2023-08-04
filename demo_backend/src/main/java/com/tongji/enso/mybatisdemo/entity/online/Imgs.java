package com.tongji.enso.mybatisdemo.entity.online;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Imgs {
    private int id;
    private String year;
    private String month;
    private String day;
    private String type;
    private String data;;
}
