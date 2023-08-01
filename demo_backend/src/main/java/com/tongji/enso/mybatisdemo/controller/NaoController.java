package com.tongji.enso.mybatisdemo.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tongji.enso.mybatisdemo.mapper.online.EnsoMapper;
import com.tongji.enso.mybatisdemo.mapper.online.NaoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nao")
public class NaoController {

    @Autowired
    private NaoMapper naoMapper;

    /**
     * 根据年月，返回该时起报，模型预报6个月的误差 （for 预测结果误差分布图）
     * eg. http://localhost:8888/nao/predictionExamination/error?year=2021&month=6
     */
    @RequestMapping("/predictionExamination/error")
    public Map<String, Object> getErrorByMonthType(String year, String month) {
        String forecastData = naoMapper.findTJNaoResultByMonthType(year, month, "grid_NAO_MCD");
        String observedData = naoMapper.findObsNaoResultByMonthType(year, month, "grid_NAO_MCD");

        Gson gson = new Gson();
        List<List<List<Double>>> forecastDataList3D = gson.fromJson(forecastData, new TypeToken<List<List<List<Double>>>>() {
        }.getType());  // 三维数组

        List<List<Double>> observedDataList2D = gson.fromJson(observedData, new TypeToken<List<List<Double>>>() {
        }.getType());  // 二维数组

        List<List<List<Double>>> errorDataList3D = new ArrayList<>();

        for (int i = 0; i < 6; i++)
        {
            List<List<Double>> errorDataList2D = new ArrayList<>();
            List<List<Double>> forecastDataList2D = forecastDataList3D.get(i);  // 取出该时起报的第i个月的预测数据

            // forecastDataList2D 和 observedDataList2D size 相同
            for (int j = 0; j < forecastDataList2D.size(); j++)
            {
                List<Double> forecastDataList1D = forecastDataList2D.get(j);  // 取出该时起报的第i个月的第j个格点的预测数据
                List<Double> observedDataList1D = observedDataList2D.get(j);  // 取出该时起报的第i个月的第j个格点的观测数据
                List<Double> errorData1DList = new ArrayList<>();

                for (int k = 0; k < forecastDataList1D.size(); k++)
                {
                    Double forecastValue = forecastDataList1D.get(k);
                    Double observedValue = observedDataList1D.get(k);
                    Double errorValue = forecastValue - observedValue;
                    errorData1DList.add(errorValue);
                }
                errorDataList2D.add(errorData1DList);
            }
            errorDataList3D.add(errorDataList2D);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", errorDataList3D);

        return result;
    }

    /**
     * 返回 tj_nao 表中 "all"，绘制三条折线 （for NAOI 指数预测的相关系数）
     * eg. http://localhost:8888/nao/predictionExamination/naoi
     */

    @RequestMapping("/predictionExamination/naoi")
    public Map<String, Object> getNaoi() {
        String allCorrECMWFData = naoMapper.findTJNaoResultByMonthType("all", "all", "corr_lead1-6_ECMWF");
        String allCorrECCCData = naoMapper.findTJNaoResultByMonthType("all", "all", "corr_lead1-6_ECCC");
        String allCorrNAOMCDData = naoMapper.findTJNaoResultByMonthType("all", "all", "corr_lead1-6_NAO-MCD");

        Gson gson = new Gson();
        List<Double> allCorrECMWFList1D = gson.fromJson(allCorrECMWFData, new TypeToken<List<Double>>() {
        }.getType());
        List<Double> allCorrECCCList1D = gson.fromJson(allCorrECCCData, new TypeToken<List<Double>>() {
        }.getType());
        List<Double> allCorrNAOMCDList1D = gson.fromJson(allCorrNAOMCDData, new TypeToken<List<Double>>() {
        }.getType());

        Map<String, Object> result = new HashMap<>();
        result.put("corrECMWFValues", allCorrECMWFList1D);
        result.put("corrECCCValues", allCorrECCCList1D);
        result.put("corrNAOMCDValues", allCorrNAOMCDList1D);

        return result;
    }

}
