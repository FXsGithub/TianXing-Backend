package com.tongji.enso.mybatisdemo.mapper.online;

import com.tongji.enso.mybatisdemo.entity.online.Meteo;
import com.tongji.enso.mybatisdemo.entity.online.Tj_sie;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Tj_sieMapper {

    /**
     * 查询全部SIE指数
     */
    List<Tj_sie> findAll();

    /**
     * 查询某年某月之后12个月的SIE指数
     */
    List<Tj_sie> findByMonth(String year, String month);
}
