package com.tongji.enso.mybatisdemo.mapper.online;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NaoMapper {

    /**
     * 从 tj_nao 表中查询指定年、月、变量模型的数据
     */
    @Select("SELECT data FROM tj_nao WHERE year = #{year} AND month = #{month} AND var_model = #{var_model}")
    String findTJNaoResultByMonthType(@Param("year") String year, @Param("month") String month, @Param("var_model") String var_model);


    /**
     * 从 obs_nao 表中查询指定年、月、变量模型的数据
     */
    @Select("SELECT data FROM obs_nao WHERE year = #{year} AND month = #{month} AND var_model = #{var_model}")
    String findObsNaoResultByMonthType(@Param("year") String year, @Param("month") String month, @Param("var_model") String var_model);
}
