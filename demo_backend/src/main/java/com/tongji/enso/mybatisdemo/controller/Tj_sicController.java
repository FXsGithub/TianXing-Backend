package com.tongji.enso.mybatisdemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongji.enso.mybatisdemo.entity.online.Tj_sic;
import com.tongji.enso.mybatisdemo.service.online.Tj_sicService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/seaice")
public class Tj_sicController {

    @Autowired
    private Tj_sicService tj_sicservice;

    /**
     * 查询全部的SIC指数
     */
    @GetMapping("/findAllSIC")
    @ApiOperation(value = "查询全部Sic指数，返回'Tj_sic'类型列表", notes = "查询全部Sic指数")
    public List<Tj_sic> findAll(){ return tj_sicservice.findAllSIC(); }

    /**
     * 查询某月份的SIC指数
     * @param: year, month, day;
     * @return: Map<String, Object>.
     */
    @GetMapping("/predictionResult/SIC")
    @ApiOperation(value = "查询月份的SIC指数以及文本描述", notes = "根据月份查询SIC指数预测结果")
    public Map<String, Object> findSICPredictionByDate(@RequestParam String year,@RequestParam String month){

        List<Tj_sic> sicList = tj_sicservice.findPredictionByDate(year,month);
        Map<String, Object> sicMap=new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        double[][][] data = null;
        // 将data字段转化为三维数组
        for(Tj_sic sic:sicList){
            try {
                String jsonString = sic.getData();
                data = objectMapper.readValue(jsonString, double[][][].class);
                sicMap.put(sic.getDay(),data);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return sicMap;
    }

    @GetMapping("/error")
    @ApiOperation(value = "查询月份4周的SIC预测结果与基线方法的比较以及文本描述", notes = "根据月份查询SIC指数预测结果与基线方法的比较")
    public Map<String, Object> findSICCheckoutByMonth(@RequestParam String year,@RequestParam String month){

        List<Tj_sic> sicList=tj_sicservice.findCheckoutByMonth(year,month);

        Map<String, Object> sicMap=new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        double []data = null;
        for(Tj_sic sic:sicList){
            try {
                String jsonString = sic.getData();
                data = objectMapper.readValue(jsonString, double[].class);
                sicMap.put(sic.getVar_model(),data);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return sicMap;
    }

    @GetMapping("/errorBox")
    public Map<String, Object> findSICErrorBoxByYear(@RequestParam String year){

        List<Tj_sic> sicList=tj_sicservice.findErrorBoxByYearAndModel(year);

        Map<String, Object> sicMap=new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();


        double []data = null;

        for(Tj_sic sic:sicList){
            try {
                String jsonString = sic.getData();
                data = objectMapper.readValue(jsonString, double[].class);
                int size= data.length / 7;
                double [][]result = new double[7][size];
                int j = 0;
                for(int i = 0; i < data.length; i++){
                    result[i % 7][j] = data[i];
                    if (i % 7 == 6) {
                        j++;
                    }
                }
                sicMap.put(sic.getVar_model(),result);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return sicMap;
    }

}
