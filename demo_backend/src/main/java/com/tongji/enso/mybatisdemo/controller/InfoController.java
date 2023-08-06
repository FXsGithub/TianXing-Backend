package com.tongji.enso.mybatisdemo.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tongji.enso.mybatisdemo.entity.online.info_sic_latlon;
import com.tongji.enso.mybatisdemo.mapper.online.InfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class InfoController {

    @Autowired
    private InfoMapper latLonMapper;

    /**
     * 获取全部经纬度数据
     */
    @RequestMapping("/getAllLatLon")
    public Map<String, List<Double>> getAllLatLon() {
        List<info_sic_latlon> latLonDataList = latLonMapper.findInfoSicLatlon();

        Gson gson = new Gson();
        List<List<Double>> latitudes = gson.fromJson(latLonDataList.get(0).getLat(), new TypeToken<List<List<Double>>>() {}.getType());
        List<List<Double>> longitudes = gson.fromJson(latLonDataList.get(0).getLon(), new TypeToken<List<List<Double>>>() {}.getType());

        // 转换为一维列表
        List<Double> latList = new ArrayList<>();
        List<Double> lonList = new ArrayList<>();

        for (List<Double> latRow : latitudes) {
            latList.addAll(latRow);
        }

        for (List<Double> lonRow : longitudes) {
            lonList.addAll(lonRow);
        }

        Map<String,List<Double>> result = new HashMap<>();

        result.put("lat", latList);
        result.put("lon", lonList);

        return result;
    }

}

