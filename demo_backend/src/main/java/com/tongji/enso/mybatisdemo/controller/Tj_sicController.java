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
    public List<Tj_sic> findAll(){ return tj_sicservice.findAllSic(); }

    /**
     * 查询某月份的SIC指数
     * @param: year, month;
     * @return: Tj_sic.
     */
    @GetMapping("/predictionResult/SIC")
    @ApiOperation(value = "查询月份的SIC指数以及文本描述", notes = "根据月份查询SIC指数预测结果")
    public Tj_sic findPredictionByYearAndMonth(@RequestParam String year, @RequestParam String month){
        return tj_sicservice.findSICPredictionByYearAndMonth(year,month);
    }

    /**
     * 格式化SIC指数
     * @param: year, month;
     * @return: HashMap<String, Object>.
     */
    @GetMapping("/transSIC")
    public HashMap<String, Object> transSICData(@RequestParam String year, @RequestParam String month){

        Tj_sic tj_sic=tj_sicservice.findSICPredictionByYearAndMonth(year,month);

        String jsonString = tj_sic.getData();

        double[][][] data_trans = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            data_trans = objectMapper.readValue(jsonString, double[][][].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HashMap<String, Object> return_hashmap=new HashMap<String, Object>();

        return_hashmap.put("data", data_trans);

        return return_hashmap;
    }

}
