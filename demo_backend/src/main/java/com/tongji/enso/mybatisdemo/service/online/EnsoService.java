package com.tongji.enso.mybatisdemo.service.online;

import com.tongji.enso.mybatisdemo.entity.online.Enso;
import com.tongji.enso.mybatisdemo.mapper.online.EnsoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EnsoService {
    @Autowired
    private EnsoMapper ensoMapper;

}
