package com.tongji.enso.mybatisdemo.service.online;

import com.tongji.enso.mybatisdemo.entity.online.info_sic_latlon;
import com.tongji.enso.mybatisdemo.mapper.online.Info_sic_latlonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Info_sic_latlonService {
    @Autowired
    private Info_sic_latlonMapper info_sic_latlonMapper;

    public info_sic_latlon findlatlon(){return info_sic_latlonMapper.findlatlon();}
}
