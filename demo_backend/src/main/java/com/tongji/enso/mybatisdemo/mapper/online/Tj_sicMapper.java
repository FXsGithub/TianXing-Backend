package com.tongji.enso.mybatisdemo.mapper.online;


import com.tongji.enso.mybatisdemo.entity.online.Tj_sic;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Tj_sicMapper {

    List<Tj_sic> findAll();

    Tj_sic findByDate(String year, String month, String day);

    List<Tj_sic> findErrorByMonth(String year, String month);

    List<Tj_sic> findErrorBoxByYearAndModel(String year);
}
