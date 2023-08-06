package com.tongji.enso.mybatisdemo.service.online;

import com.tongji.enso.mybatisdemo.entity.online.Obs_nao;
import com.tongji.enso.mybatisdemo.mapper.online.Obs_naoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Obs_naoService {

    @Autowired
    private Obs_naoMapper obs_naomapper;

    public Obs_nao findObservationByMonthAndModel(String year, String month){
        return obs_naomapper.findByMonthAndModel(year,month);
    }

    public List<Obs_nao> findNAOByModel(String var_model){
        return obs_naomapper.findAllByModel(var_model);
    }

}
