package com.tongji.enso.mybatisdemo.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tongji.enso.mybatisdemo.entity.online.Imgs;
import com.tongji.enso.mybatisdemo.mapper.online.ImgsMapper;
import com.tongji.enso.mybatisdemo.mapper.online.ImgsMapperEnso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/imgs")
public class ImgsController {

    @Autowired
    private ImgsMapper imgsMapper;
    @Autowired
    private ImgsMapperEnso ImgsMapperEnso;

    /**
     * 获取指定 年、月、日 的图片路径 WEA_MSLP
     * eg. http://localhost:8080/imgs/WEA_MSLP/getImgsPath?year=2019&month=1&day=1
     * @param year
     * @param month
     * @param day
     * @return
     */
    @RequestMapping("/WEA_MSLP/getImgsPath")
    public Map<String, Object> getMSLPImgsPathByDay(String year, String month, String day) {
        List<Imgs> imgsData = imgsMapper.findImgsInfoByDayType(year, month, day, "WEA_MSLP");

        Map<String, Object> result = new HashMap<>();
        result.put("data", imgsData);
        return result;
    }

    /**
     * 获取指定 年、月、日 的图片路径 WEA_T2M
     * eg. http://localhost:8080/imgs/WEA_T2M/getImgsPath?year=2019&month=1&day=1
     * @param year
     * @param month
     * @param day
     * @return
     */
    @RequestMapping("/WEA_T2M/getImgsPath")
    public Map<String, Object> getT2MImgsPathByDay(String year, String month, String day) {
        List<Imgs> imgsData = imgsMapper.findImgsInfoByDayType(year, month, day, "WEA_T2M");

        Map<String, Object> result = new HashMap<>();
        result.put("data", imgsData);
        return result;
    }

    /**
     * 获取指定 年、月、日 的图片路径 WEA_TP
     * eg. http://localhost:8080/imgs/WEA_TP/getImgsPath?year=2019&month=1&day=1
     */
    @RequestMapping("/WEA_TP/getImgsPath")
    public Map<String, Object> getTPImgsPathByDay(String year, String month, String day) {
        List<Imgs> imgsData = imgsMapper.findImgsInfoByDayType(year, month, day, "WEA_TP");

        Map<String, Object> result = new HashMap<>();
        result.put("data", imgsData);
        return result;
    }

    /**
     * 获取指定 年、月、日 的图片路径 WEA_U10
     * eg. http://localhost:8080/imgs/WEA_U10/getImgsPath?year=2019&month=1&day=1
     */

    @RequestMapping("/WEA_U10/getImgsPath")
    public Map<String, Object> getU10ImgsPathByDay(String year, String month, String day) {
        List<Imgs> imgsData = imgsMapper.findImgsInfoByDayType(year, month, day, "WEA_U10");

        Map<String, Object> result = new HashMap<>();
        result.put("data", imgsData);
        return result;
    }
    /**
     * 获取指定 年、月、index的图片路径
     * eg. http://localhost:9090/imgs/WEA_U10/getImgsPath?year=2019&month=1&day=1
     */
    @RequestMapping("/predictionResult/ssta")
    public Map<String,  Object> getSstaData(String year, String month, String day) {
        List<Imgs> imgsData = ImgsMapperEnso.findImgsInfoByDayType(year, month,"WEA_U10");

        Map<String, Object> result = new HashMap<>();
        result.put("data", imgsData);
        return result;
    }

}
