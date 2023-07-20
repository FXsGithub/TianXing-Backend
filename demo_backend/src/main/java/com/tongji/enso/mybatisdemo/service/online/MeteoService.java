package com.tongji.enso.mybatisdemo.service.online;

import com.tongji.enso.mybatisdemo.entity.online.Meteo;
import com.tongji.enso.mybatisdemo.mapper.online.MeteoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeteoService {

    @Autowired
    private MeteoMapper meteoMapper;

//    命名要有具体含义
    public List<Meteo> findAllMeteo(){
        return meteoMapper.findAll();
    }

    public Meteo findById(int id){
        return meteoMapper.findById(id);
    }

}
