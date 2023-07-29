package com.tongji.enso.mybatisdemo.service.online;

import com.tongji.enso.mybatisdemo.entity.online.Tj_nao;
import com.tongji.enso.mybatisdemo.mapper.online.Tj_naoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Tj_naoService {

    @Autowired
    private Tj_naoMapper tj_naomapper;

    public Tj_nao findPredictionByMonthAndModel(String year, String month){
        return tj_naomapper.findByMonthAndModel(year,month);
    }
}
