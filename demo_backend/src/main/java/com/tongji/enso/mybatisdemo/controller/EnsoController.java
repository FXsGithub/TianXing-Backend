package com.tongji.enso.mybatisdemo.controller;

import com.tongji.enso.mybatisdemo.mapper.online.EnsoMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.lang.reflect.Type;
import java.util.*;

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
    public Map<String, List<List<Double>>> getSstaData(@RequestParam("year") String year, @RequestParam("month") String month, @RequestParam("monthIndex") int monthIndex) {
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

    /**
     * 根据指定的年份和月份，返回当前月份及其前11个月（总共12个月）的数据 预测值 + 实际值
     * TODO：添加预测值，不知道该返回什么数据
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/predictionExamination/monthlyComparison")
    public Map<String, List<Double>> getMonthlyComparison(@RequestParam("year") String year, @RequestParam("month") String month)
    {
        Gson gson = new Gson();

        Type listType = new TypeToken<List<Double>>() {
        }.getType();

        // 使用给定年份查询所有数据
        String currentYearResult = ensoMapper.findObsEnsoByYear(year);
        List<Double> currentYearData = gson.fromJson(currentYearResult, listType);
        // 创建一个包含12个-1的列表
        List<Double> currentYearNewData = new ArrayList<>(Collections.nCopies(12, -1.0));
        // 用查询到的数据替换默认数据
        for (int i = 0; i < currentYearData.size(); i++) {
            currentYearNewData.set(i, currentYearData.get(i));
        }

        // 查询前一年的数据
        String previousYear = String.valueOf(Integer.parseInt(year) - 1);
        String previousYearResult = ensoMapper.findObsEnsoByYear(previousYear);

        // 如果前一年没有数据，那么返回包含 12 个 -1 的列表
        List<Double> previousYearNewData = new ArrayList<>(Collections.nCopies(12, -1.0));

        if (previousYearResult != null && !previousYearResult.isEmpty())
        {
            List<Double> previousYearData = gson.fromJson(previousYearResult, listType);

            // 用查询到的数据替换默认数据
            for (int i = 0; i < previousYearData.size(); i++) {
                previousYearNewData.set(i, previousYearData.get(i));
            }
        }

        // 合并两个年份的数据
        previousYearNewData.addAll(currentYearNewData);

        // 根据给定的月份，选择最近的12个月的数据
        int givenMonth = Integer.parseInt(month);
        int startIndex = givenMonth;
        int endIndex = givenMonth + 12;

        List<Double> previous12MonthsData = previousYearNewData.subList(startIndex, endIndex);

        Map<String, List<Double>> resultMap = new HashMap<>();
        resultMap.put("obs", previous12MonthsData);

//        Map<String, List<Double>> lineChartData = getLineChartData(previousYear, month);
//        resultMap.putAll(lineChartData);
        return resultMap;
    }
}
