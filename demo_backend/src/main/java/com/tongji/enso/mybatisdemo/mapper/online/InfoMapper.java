package com.tongji.enso.mybatisdemo.mapper.online;

import com.tongji.enso.mybatisdemo.entity.online.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InfoMapper {

    /**
     * 从 info_sic_latlon 表中查询全部经纬度数据
     */
    @Select("SELECT * FROM info_sic_latlon")
    List<Info_sic_latlon> findInfoSicLatlon();


}
