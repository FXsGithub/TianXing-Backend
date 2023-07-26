package com.tongji.enso.mybatisdemo.mapper.online;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface EnsoMapper {

    /**
     * 从 tj_enso 表中查询指定年、月、变量模型的数据
     * @param year
     * @param month
     * @param var_model
     * @return
     */
    @Select("SELECT data FROM tj_enso WHERE year = #{year} AND month = #{month} AND var_model = #{var_model}")
    String findEachPredictionsResultByMonthType(@Param("year") String year, @Param("month") String month, @Param("var_model") String var_model);


}
