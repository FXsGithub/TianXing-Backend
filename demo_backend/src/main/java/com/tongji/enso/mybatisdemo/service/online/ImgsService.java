package com.tongji.enso.mybatisdemo.service.online;

import com.tongji.enso.mybatisdemo.entity.online.Imgs;
import com.tongji.enso.mybatisdemo.mapper.online.ImgsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImgsService {

    @Autowired
    private ImgsMapper imgsmapper;

    public String findSICImgByDate(String year, String month, String day){
        return imgsmapper.findSICByDate(year,month,day);
    }

    public String findNAOImgByMonth(String year, String month){
        return imgsmapper.findNAOByMonth(year,month);
    }

    public List<Imgs> findAllByType(String type){
        return imgsmapper.findAllByType(type);
    }

    public String findNAOCORRImgByMonth(String year, String month){
        return imgsmapper.findNAOCORRByMonth(year,month);
    }
}
