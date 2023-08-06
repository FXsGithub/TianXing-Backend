package com.tongji.enso.mybatisdemo.mapper.online;

import com.tongji.enso.mybatisdemo.entity.online.Imgs;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImgsMapper {

    String findSICByDate(String year, String month, String day);

    String findNAOByMonth(String year, String month);

    List<Imgs> findAllByType(String type);
}
