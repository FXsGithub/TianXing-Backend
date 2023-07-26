package com.tongji.enso.mybatisdemo.controller;

import com.tongji.enso.mybatisdemo.mapper.online.EnsoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 控制层：与前端进行交互

@RestController
@RequestMapping("/enso")
public class EnsoController {

    @Autowired
    private EnsoMapper ensoMapper;

    /**
     * For Niño3.4指数预测结果
     * 从 tj_enso 表中查询指定年、月的 Nino34（即 ENSO 指数） 数据（五种模型）
     *
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/predictionResult/linechart")
    public Map<String, List<Double>> getLineChartData(@RequestParam("year") String year, @RequestParam("month") String month) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Double>>() {
        }.getType();

        String result1 = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_asc");  // 从数据库中查询数据
        List<Double> list1 = gson.fromJson(result1, listType);  // 将结果字符串转换为列表

        String result2 = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_gtc");
        List<Double> list2 = gson.fromJson(result2, listType);

        String result3 = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_cross");
        List<Double> list3 = gson.fromJson(result3, listType);

        String result4 = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_mc");
        List<Double> list4 = gson.fromJson(result4, listType);

        String result5 = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_mean");
        List<Double> list5 = gson.fromJson(result5, listType);

        Map<String, List<Double>> resultMap = new HashMap<>();
        resultMap.put("nino34_asc", list1);  // key 为模型名，value 为对应的数据列表
        resultMap.put("nino34_gtc", list2);
        resultMap.put("nino34_cross", list3);
        resultMap.put("nino34_mc", list4);
        resultMap.put("nino34_mean", list5);

        return resultMap;

    }


    /**
     * for Niño3.4区SST集合平均预测结果  --- 学长说使用 xxxssta 数据绘制
     * http://localhost:8888/enso/predictionResult/ssta?year=2023&month=4&monthIndex=1
     * @param year
     * @param month
     * @param monthIndex
     * @return
     */
    @GetMapping("/predictionResult/ssta")
    public Map<String, List<List<Double>>> getSstData(@RequestParam("year") String year, @RequestParam("month") String month, @RequestParam("monthIndex") int monthIndex) {

        String result = ensoMapper.findEachPredictionsResultByMonthType(year, month, "ssta_mean");

        // 将结果字符串转换为三维列表
        Gson gson = new Gson();
        List<List<List<Double>>> resultList = gson.fromJson(result, new TypeToken<List<List<List<Double>>>>(){}.getType());

        // 将结果添加到 Map 中并返回
        Map<String, List<List<Double>>> resultMap = new HashMap<>();
        if(monthIndex >= 0 && monthIndex < resultList.size()) {
            resultMap.put("ssta_mean", resultList.get(monthIndex));
        }

        return resultMap;

    }





}
