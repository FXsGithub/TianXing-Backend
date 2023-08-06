package com.tongji.enso.mybatisdemo.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tongji.enso.mybatisdemo.mapper.online.NaoMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@RequestMapping("/nao")
public class NaoController {

    @Autowired
    private NaoMapper naoMapper;

    private final InfoController infoController;

    public NaoController(InfoController infoController) {
        this.infoController = infoController;
    }


    /**
     * 创建系列数据对象
     * @param name
     * @param type
     * @param lineType
     * @param data
     * @return
     */
    private <T> Map<String, Object> createSeries(String name, String type, String lineType, List<T> data) {
        Map<String, Object> series = new HashMap<>();
        series.put("name", name);
        series.put("type", type);
        if (lineType != null) {
            Map<String, Object> lineStyle = new HashMap<>();
            lineStyle.put("type", lineType);
            series.put("lineStyle", lineStyle);
        }
        series.put("data", data);
        return series;
    }


    /**
     * 根据年月，返回该时起报，模型预报6个月的误差 （for 预测结果误差分布图）
     * eg. http://localhost:8888/nao/predictionExamination/error?year=2021&month=6
     */
    @GetMapping("/predictionExamination/error")
    @ApiOperation(value = "根据年月，返回该时起报，模型预报6个月的误差")
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

        // 获取经纬度数据
        Map<String, List<Double>> latLonData = infoController.getAllLatLon();
        List<Double> latList = latLonData.get("lat");
        List<Double> lonList = latLonData.get("lon");

        Map<String, Object> result = new HashMap<>();  // 存放总的六个 option

        List<Double> xData = new ArrayList<>();
        List<Double> yData = new ArrayList<>();
        for (double i = 1; i <= 13; i++) {
            xData.add(i);
        }
        for (double j = 1; j <= 27; j++)
        {
            yData.add(j);
        }

        for (int i = 0; i < 6; i++)
        {
            Map<String, Object> eachResult = new HashMap<>();  // 存放当 option，利用 for 循环更改其中的数据

            Map<String, Object> title = new HashMap<>();
            title.put("text", "提前" + (i + 1) + "个月预测");
            title.put("left", "center");
            eachResult.put("title", title);

            Map<String, Object> tooltip = new HashMap<>();
            eachResult.put("tooltip", tooltip);

            Map<String, Object> xAxis = new HashMap<>();
            xAxis.put("type", "category");
            xAxis.put("name", "经度");
            xAxis.put("data", xData);
            eachResult.put("xAxis", xAxis);

            Map<String, Object> yAxis = new HashMap<>();
            yAxis.put("type", "category");
            yAxis.put("name", "纬度");
            yAxis.put("data", yData);
            eachResult.put("yAxis", yAxis);

            eachResult.put("series", createSeries("ours", "heatmap", null, errorDataList3D.get(i)));

            result.put("option" + (i + 1), eachResult);
        }

        Map<String, Object> resultw = new HashMap<>();  // 存放总的六个 option
        resultw.put("try", errorDataList3D.get(0));

        return resultw;
    }

    /**
     * 返回 tj_nao 表中 "all"，绘制三条折线 （for NAOI 指数预测的相关系数）
     * eg. http://localhost:8888/nao/predictionExamination/naoi
     */
    @GetMapping("/predictionExamination/naoi")
    @ApiOperation(value = "返回 tj_nao 表中 'all'，绘制三条折线")
    public Map<String, Object> getNaoi() {
        String allCorrECMWFData = naoMapper.findTJNaoResultByMonthType("all", "all", "corr_lead1-6_ECMWF");
        String allCorrECCCData = naoMapper.findTJNaoResultByMonthType("all", "all", "corr_lead1-6_ECCC");
        String allCorrNAOMCDData = naoMapper.findTJNaoResultByMonthType("all", "all", "corr_lead1-6_NAO-MCD");

        Gson gson = new Gson();
        List<Double> allCorrECMWFList1D = gson.fromJson(allCorrECMWFData, new TypeToken<List<Double>>() {}.getType());
        List<Double> allCorrECCCList1D = gson.fromJson(allCorrECCCData, new TypeToken<List<Double>>() {}.getType());
        List<Double> allCorrNAOMCDList1D = gson.fromJson(allCorrNAOMCDData, new TypeToken<List<Double>>() {}.getType());

        Map<String, Object> result = new HashMap<>();


        Map<String, Object> title = new HashMap<>();
        title.put("text", "月度NAOI预测技巧");
        title.put("left", "center");
        result.put("title", title);

        Map<String, Object> tooltip = new HashMap<>();
        result.put("tooltip", tooltip);

        Map<String, Object> xAxis = new HashMap<>();
        xAxis.put("type", "category");
        xAxis.put("name", "预测提前期（月）");
        Map<String, Object> axisLine = new HashMap<>();
        Map<String, Object> lineStyle = new HashMap<>();
        lineStyle.put("color", "black");
        axisLine.put("lineStyle", lineStyle);
        axisLine.put("onZero", false);
        xAxis.put("axisLine", axisLine);
        xAxis.put("data", Arrays.asList("1", "2", "3", "4", "5", "6"));
        result.put("xAxis", xAxis);

        Map<String, Object> yAxis = new HashMap<>();
        yAxis.put("type", "value");
        yAxis.put("name", "相关系数技巧");
        yAxis.put("min", -0.2);
        yAxis.put("data", Arrays.asList(-0.2, 0.0, 0.2, 0.4, 0.6, 0.8));
        result.put("yAxis", yAxis);

        Map<String, Object> legend = new HashMap<>();
        legend.put("data", Arrays.asList("95% significance", "ECMWF", "ECCC", "NAO-MCR"));
        legend.put("orient", "horizontal");
        legend.put("left", "center");
        legend.put("bottom", "5");
        result.put("legend", legend);

        List<Map<String, Object>> series = Arrays.asList(
                createSeries("95% significance", "line", "dashed", Arrays.asList(0.35, 0.35, 0.35, 0.35, 0.35, 0.35)),
                createSeries("ECMWF", "line", null, allCorrECMWFList1D),
                createSeries("ECCC", "line", null, allCorrECCCList1D),
                createSeries("NAO-MCR", "line", null, allCorrNAOMCDList1D)
        );
        result.put("series", series);

        return result;
    }
}
