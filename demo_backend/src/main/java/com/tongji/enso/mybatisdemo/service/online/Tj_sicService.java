package com.tongji.enso.mybatisdemo.service.online;

import com.tongji.enso.mybatisdemo.entity.online.Tj_sic;
import com.tongji.enso.mybatisdemo.mapper.online.Tj_sicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Tj_sicService {

    @Autowired
    private Tj_sicMapper tj_sicmapper;

    // Tj_sic findByYAndM(String year, String month);

}
