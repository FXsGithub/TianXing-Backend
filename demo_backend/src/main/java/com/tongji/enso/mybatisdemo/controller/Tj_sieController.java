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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seaice")
public class Tj_sieController {
    @Autowired
    private Tj_sieService tj_sieService;

    /**
     * 查询全部SIE指数
     */
    @GetMapping("/findAll/SIE")
    @ApiOperation(notes = "查询所有月份的SIE指数，按id排序", value = "查询全部SIE指数")
    public List<Tj_sie> findAll(){
        return tj_sieService.findAllSIE();
    }

    /**
     * 查询某月份开始起报之后12个月的SIE指数
     */
    @GetMapping("/predictionResult/SIE")
    @ApiOperation(notes = "查询月份开始起报之后12个月的SIE指数以及文本描述", value = "根据月份查询SIE指数预测结果")
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
    @GetMapping("/findByModelandTime/SIE")
    @ApiOperation(notes = "查询指定var_model以及指定年月的SIE数据(已转化为一维数组形式）", value = "根据年月和var_model查询SIE指数预测结果")
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

    /**
     * 查询年份及其前几年的rmsd和相关系数等指标的数据，文本描述
     */
    @GetMapping("/predictionExamination/errorAnalysis")
    @ApiOperation(value = "SIE预测误差分析", notes = "查询年份及其前几年的rmsd和相关系数等指标的数据，文本描述")
    public HashMap<String ,Object> findErrorAnalysis(@RequestParam String year){
        // 要返回的对象列表
        List<Tj_sie> sieList = tj_sieService.findByYear(year);
        HashMap<String, Object> return_hashmap = new HashMap<String, Object>();
        ObjectMapper objectMapper = new ObjectMapper();
        double[] dataArray = null;
        // 遍历返回结果中的每个Tj_sie对象，对其data字段进行解析，并替换为一维数组
        for (Tj_sie sie : sieList) {
            String jsonData = sie.getData(); // 获取JSON数据的字符串形式
            try {
                // 将JSON数据转换为一维double数组
                dataArray = objectMapper.readValue(jsonData, double[].class);
                return_hashmap.put(sie.getVar_model(), dataArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return return_hashmap;
    }

    /**
     * 查询SIE预测结果图的可查询日期和最新预报
     */
    @GetMapping("/initial/SIEprediction")
    @ApiOperation(value = "SIE可查询日期与最新预报结果", notes = "查询SIE指数预测结果图的可查询日期和最新预报")
    public HashMap<String ,Object> initialSIEprediction(){
        List<String> yearList = Arrays.asList("2020", "2021", "2022", "2023");
        List<String> monthList=Arrays.asList("1");
        // 要返回的HashMap
        HashMap<String, Object> return_hashmap = new HashMap<String, Object>();
        return_hashmap.put("yearList",yearList);
        return_hashmap.put("monthList",monthList);

        List<Tj_sie> sieList = tj_sieService.findSIEByMonth("2023", "1");
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
        return_hashmap.put("sieInitial",sieList);

        return return_hashmap;
    }

    /**
     * 查询SIE预测结果误差折线图的可查询日期和最新结果
     */
    @GetMapping("/initial/SIEErrorAnalysis")
    @ApiOperation(notes = "SIE预测误差分析可查询日期和最新结果", value = "查询SIE误差分析图的可查询日期和最新结果")
    public HashMap<String,Object> initialSIEerrorAnalysis(){
        List<String> yearList=Arrays.asList("2020-2022");
        List<String> monthList=Arrays.asList("1");
        // 要返回的HashMap
        HashMap<String, Object> return_hashmap = new HashMap<String, Object>();
        return_hashmap.put("yearList",yearList);
        return_hashmap.put("monthList",monthList);

        Map<String,Object> SIEerrorList =findErrorAnalysis("2020-2022");
        return_hashmap.put("SIEerrorInitial",SIEerrorList);

        return return_hashmap;
    }
}
