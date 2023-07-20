package com.tongji.enso.mybatisdemo.mapper.online;


import com.tongji.enso.mybatisdemo.entity.online.Meteo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeteoMapper {

    /**
     * 查询全部气象数据
     */
    List<Meteo> findAll();

    /**
     * 根据ID查询气象数据
     * @param: id;
     * @return: Meteo.
     */
    Meteo findById(int id);
}
