package com.tongji.enso.mybatisdemo.mapper.online;

import org.springframework.stereotype.Repository;

@Repository
public interface ImgsMapper {

    String findSICByDate(String year, String month, String day);

    String findNAOByMonth(String year, String month);
}
