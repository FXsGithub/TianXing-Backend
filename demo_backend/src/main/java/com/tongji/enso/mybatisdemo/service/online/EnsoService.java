package com.tongji.enso.mybatisdemo.service.online;

import com.tongji.enso.mybatisdemo.mapper.online.EnsoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnsoService {
    @Autowired
    private EnsoMapper ensoMapper;


//    List<Enso> findPredictionsByMonthType();

}
