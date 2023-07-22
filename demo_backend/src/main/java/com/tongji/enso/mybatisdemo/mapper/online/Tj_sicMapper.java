package com.tongji.enso.mybatisdemo.mapper.online;


import com.tongji.enso.mybatisdemo.entity.online.Tj_sic;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Tj_sicMapper {

    List<Tj_sic> findAll();

    Tj_sic findPredictionByYearAndMonth(String year, String month);
}
