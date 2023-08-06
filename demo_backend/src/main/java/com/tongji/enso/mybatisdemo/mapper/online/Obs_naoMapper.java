package com.tongji.enso.mybatisdemo.mapper.online;

import com.tongji.enso.mybatisdemo.entity.online.Obs_nao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Obs_naoMapper {

    Obs_nao findByMonthAndModel(String year, String month);

    List<Obs_nao> findAllByModel(String var_model);

}
