package com.tongji.enso.mybatisdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongji.enso.mybatisdemo.entity.online.Meteo;
import com.tongji.enso.mybatisdemo.entity.online.Tj_sie;
import com.tongji.enso.mybatisdemo.service.online.Tj_sieService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/seaice")
public class Tj_sieController {
    @Autowired
    private Tj_sieService tj_sieService;

    /**
     * 查询全部SIE指数
     */
    @GetMapping("/findAll/SIE")
    @ApiOperation(value = "查询所有月份的SIE指数，按id排序", notes = "查询全部SIE指数")
    public List<Tj_sie> findAll(){
        return tj_sieService.findAllSIE();
    }

    /**
     * 查询某月份开始起报之后12个月的SIE指数
     */
    @GetMapping("/predictionResult/SIE")
    @ApiOperation(value = "查询月份开始起报之后12个月的SIE指数以及文本描述", notes = "根据月份查询SIE指数预测结果")
    public List<Tj_sie> findByMonth(@RequestParam String year, @RequestParam String month){

        // 要返回的对象列表
        List<Tj_sie> sieList = tj_sieService.findSIEByMonth(year, month);
        // 使用ObjectMapper进行JSON数据解析
        ObjectMapper objectMapper = new ObjectMapper();
        // 遍历返回结果中的每个Tj_sie对象，对其data字段进行解析，并替换为一维数组
        for (Tj_sie sie : sieList) {
            String jsonData = sie.getData(); // 获取JSON数据的字符串形式
            try {
                // 将JSON数据转换为一维double数组
                double[] dataArray = objectMapper.readValue(jsonData, double[].class);
                // 将解析后的一维数组设置到Tj_sie对象的data字段中
                sie.setTrans_data(dataArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sieList;
    }

    /**
     * 查询指定var_model以及指定月份月份开始起报之后12个月的SIE数据
     */
    @GetMapping("/findByModelandMonth/SIE")
    @ApiOperation(value = "查询指定var_model以及指定月份月份开始起报之后12个月的SIE数据(已转化为一维数组形式）", notes = "根据月份和var_model查询SIE指数预测结果")
    public HashMap<String,Object> findByModelandMonth(@RequestParam String year, @RequestParam String month, @RequestParam String var_model){

        // 要返回的对象列表
        List<Tj_sie> sieList = tj_sieService.findByModelandMonth(year, month,var_model);
        ObjectMapper objectMapper = new ObjectMapper();
        double[] dataArray = null;
        // 遍历返回结果中的每个Tj_sie对象，对其data字段进行解析，并替换为一维数组
        for (Tj_sie sie : sieList) {
            String jsonData = sie.getData(); // 获取JSON数据的字符串形式
            try {
                // 将JSON数据转换为一维double数组
                dataArray = objectMapper.readValue(jsonData, double[].class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        HashMap<String, Object> return_hashmap = new HashMap<String, Object>();
        return_hashmap.put("data", dataArray);
        return return_hashmap;
    }
}
