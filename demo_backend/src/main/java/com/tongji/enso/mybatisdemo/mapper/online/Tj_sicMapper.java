package com.tongji.enso.mybatisdemo.mapper.online;


import com.tongji.enso.mybatisdemo.entity.online.Tj_sic;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Tj_sicMapper {

    /**
     * 查询全部SIC指数
     */
    List<Tj_sic> findAll();

    /**
     * 根据年月查询SIC指数
     */
    List<Tj_sic> findByDate(String year, String month);

    /**
     * 根据年月查询SIC预测结果与基线方法的比较
     */
    List<Tj_sic> findErrorByMonth(String year, String month);

    /**
     * 根据年份查询SIC提前1到7天的统计结果误差
     * */
    List<Tj_sic> findErrorBoxByYearAndModel(String year);
}
