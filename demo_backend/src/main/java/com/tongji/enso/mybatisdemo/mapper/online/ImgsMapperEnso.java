package com.tongji.enso.mybatisdemo.mapper.online;

import com.tongji.enso.mybatisdemo.entity.online.Imgs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface ImgsMapperEnso {

    /**
     * 从 imgs 中查询指定年、月、日、类型的数据
     * @return
     */
    @Select("SELECT * FROM imgs WHERE year = #{year} AND month = #{month} AND type = 'ENSO'")
    List<Imgs> findImgsInfoByDayType(@RequestParam("year") String year, @RequestParam("month") String month, @RequestParam("type") String type);

}