package com.tongji.enso.mybatisdemo.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.tongji.enso.mybatisdemo.entity.online.Meteo;
import com.tongji.enso.mybatisdemo.service.online.MeteoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/meteo")
public class MeteoController {


    /**
     * 这里简单展示了一个例子，具体开发中会涉及到更复杂的数据结构，也会涉及到要将返回数据处理成前端echarts要求的数据格式；
     * 此处只是说明代码规范；
     */

    @Autowired
    private MeteoService meteoService;

    /**
     * 查询全部气象事件
     */
    @GetMapping("/findAll")
    @ApiOperation(value = "查询全部气象事件，返回'气象事件'类型列表", notes = "查询全部气象事件")
    public List<Meteo> findAll(){
        return meteoService.findAllMeteo();
    }

    /**
     * 根据ID查询气象事件
     * @param: id;
     * @return: Meteo.
     */
    @GetMapping("/findById/{id}")
    @ApiOperation(value = "根据ID查询气象事件，返回查询的'气象事件'", notes = "根据ID查询气象事件")
    public Meteo findById(@PathVariable int id){
        return meteoService.findById(id);
    }

    /**
     * 数据格式处理demo
     */
    @GetMapping("/trans/{id}")
    @ApiOperation(value = "数据格式处理demo", notes = "JSON字符串转成多维数组的demo")
    public HashMap<String, Object> transDataById(@PathVariable int id){

        Meteo meteo = meteoService.findById(id);
        String jsonString = meteo.getData();
        double[][][] data_trans = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            data_trans = objectMapper.readValue(jsonString, double[][][].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HashMap<String, Object> return_hashmap = new HashMap<String, Object>();
        return_hashmap.put("data", data_trans);

        return return_hashmap;
    }

}
