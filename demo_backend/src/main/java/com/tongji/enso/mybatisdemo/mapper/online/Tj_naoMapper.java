package com.tongji.enso.mybatisdemo.mapper.online;

import com.tongji.enso.mybatisdemo.entity.online.Tj_nao;
import com.tongji.enso.mybatisdemo.entity.online.Tj_sic;
import org.springframework.stereotype.Repository;

@Repository
public interface Tj_naoMapper {

    Tj_nao findByMonthAndModel(String year, String month);

    Tj_nao findGridByMonth(String year, String month);
}
