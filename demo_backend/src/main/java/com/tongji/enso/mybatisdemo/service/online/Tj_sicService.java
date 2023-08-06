package com.tongji.enso.mybatisdemo.service.online;

import com.tongji.enso.mybatisdemo.entity.online.Tj_sic;
import com.tongji.enso.mybatisdemo.mapper.online.Tj_sicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Tj_sicService {

    @Autowired
    private Tj_sicMapper tj_sicmapper;

    public List<Tj_sic> findAllSIC(){ return tj_sicmapper.findAll(); }

    public Tj_sic findPredictionByDate(String year, String month,String day){
        return tj_sicmapper.findByDate(year, month,day);
    }

    public List<Tj_sic> findErrorByMonth(String year, String month){
        return tj_sicmapper.findErrorByMonth(year, month);
    }

    public List<Tj_sic> findErrorBoxByYearAndModel(String year){
        return tj_sicmapper.findErrorBoxByYearAndModel(year);
    }
}
