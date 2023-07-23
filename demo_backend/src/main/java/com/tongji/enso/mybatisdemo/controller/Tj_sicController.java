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

import java.util.HashMap;
import java.util.List;

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
     * @param: year, month;
     * @return: Tj_sic.
     */
    @GetMapping("/predictionResult/SIC")
    @ApiOperation(value = "查询月份的SIC指数以及文本描述", notes = "根据月份查询SIC指数预测结果")
    public List<Tj_sic> findSICPredictionByYearAndMonth(@RequestParam String year, @RequestParam String month){

        List<Tj_sic> sicList = tj_sicservice.findPredictionByYearAndMonth(year,month);

        ObjectMapper objectMapper = new ObjectMapper();
        double[][][] data = null;

        // 遍历查找到的每个Tj_sic对象，对其data字段进行解析，并替换为三维数组
        for(Tj_sic sic:sicList){
            String jsonString = sic.getData();
            try {
                data = objectMapper.readValue(jsonString, double[][][].class);
                sic.setTrans_data(data);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return sicList;
    }
}
