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
    public Map<String, List<Double>> findNino34PredictionsByMonth(@RequestParam("year") String year, @RequestParam("month") String month) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Double>>() {
        }.getType();

        String result1 = ensoMapper.findEachPredictionsByMonthType(year, month, "nino34_asc");
        List<Double> list1 = gson.fromJson(result1, listType);

        String result2 = ensoMapper.findEachPredictionsByMonthType(year, month, "nino34_gtc");
        List<Double> list2 = gson.fromJson(result2, listType);

        String result3 = ensoMapper.findEachPredictionsByMonthType(year, month, "nino34_cross");
        List<Double> list3 = gson.fromJson(result3, listType);

        String result4 = ensoMapper.findEachPredictionsByMonthType(year, month, "nino34_mc");
        List<Double> list4 = gson.fromJson(result4, listType);

        String result5 = ensoMapper.findEachPredictionsByMonthType(year, month, "nino34_mean");
        List<Double> list5 = gson.fromJson(result5, listType);

        Map<String, List<Double>> resultMap = new HashMap<>();
        resultMap.put("nino34_asc", list1);
        resultMap.put("nino34_gtc", list2);
        resultMap.put("nino34_cross", list3);
        resultMap.put("nino34_mc", list4);
        resultMap.put("nino34_mean", list5);

        return resultMap;

    }

}
