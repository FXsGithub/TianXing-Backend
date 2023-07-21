package com.tongji.enso.mybatisdemo.mapper.online;

import com.tongji.enso.mybatisdemo.entity.online.Enso;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EnsoMapper {

    /**
     * 查询 tj_enso 表中全部数据
     * @return
     */
    @Select("select * from tj_enso")
    List<Enso> findAll();


    /**
     * 从 tj_enso 表中查询接下来 18 个月的预测数据
     * @param year
     * @param month
     * @return
     */
    @Select("SELECT id FROM tj_enso WHERE year >= #{year} AND (year > #{year} OR month >= #{month}) LIMIT 18")
    List<Enso> findPredictionsForNext18Months(@Param("year") String year, @Param("month") String month);

}
