package com.tongji.enso.mybatisdemo.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tongji.enso.mybatisdemo.entity.online.*;
import com.tongji.enso.mybatisdemo.mapper.online.InfoMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/getAllLatLon")
    @ApiOperation(value = "获取全部经纬度数据", notes = "获取全部经纬度数据")
    public Map<String, List<Double>> getAllLatLon() {
        List<Info_sic_latlon> latLonDataList = latLonMapper.findInfoSicLatlon();

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

    /**
     * 测试用
     */
    @GetMapping("/getTest")
    @ApiOperation(value = "测试用", notes = "测试用")
    public Map<String, List<Double>> getTest() {

        // 转换为一维列表
        List<Double> latList = new ArrayList<>();
        List<Double> lonList = new ArrayList<>();

        latList.add(2.0);
        latList.add(3.0);

        Map<String,List<Double>> result = new HashMap<>();

        result.put("lat", latList);
        result.put("lon", lonList);

        return result;
    }

}

