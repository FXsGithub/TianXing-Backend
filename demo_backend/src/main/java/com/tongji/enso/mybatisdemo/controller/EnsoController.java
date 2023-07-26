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
import java.util.stream.Collectors;

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

        // 查询下一年的数据
        String futureYear = String.valueOf(Integer.parseInt(year) + 1);
        String futureYearResult = ensoMapper.findObsEnsoByYear(futureYear);

        // 如果下一年没有数据，那么返回包含 12 个 -1 的列表
        List<Double> futureYearNewData = new ArrayList<>(Collections.nCopies(12, -1.0));

        if (futureYearResult != null && !futureYearResult.isEmpty())
        {
            List<Double> previousYearData = gson.fromJson(futureYearResult, listType);

            // 用查询到的数据替换默认数据
            for (int i = 0; i < previousYearData.size(); i++) {
                futureYearNewData.set(i, previousYearData.get(i));
            }
        }

        // 合并两个年份的数据
        currentYearNewData.addAll(futureYearNewData);

        // 根据给定的月份，选择最近的12个月的数据
        int givenMonth = Integer.parseInt(month);
        int startIndex = givenMonth;  // 不包含本月
        int endIndex = givenMonth + 12;

        List<Double> current12MonthsData = currentYearNewData.subList(startIndex, endIndex);

        Map<String, List<Double>> resultMap = new HashMap<>();
        resultMap.put("obs", current12MonthsData);

        Map<String, List<Double>> lineChartData = getLineChartData(year, month);

        // 只要前12个月的预测数据
        Map<String, List<Double>> filteredLineChartData = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : lineChartData.entrySet()) {
            List<Double> fullData = entry.getValue();

            List<Double> last12MonthsData = fullData.subList(0, 12);
            filteredLineChartData.put(entry.getKey(), last12MonthsData);
        }

        resultMap.putAll(filteredLineChartData);

        return resultMap;
    }

    /**
     * 返回数据列表的箱型图数据（最小值、Q1、中位数、Q3、最大值）
     * @param data
     * @return
     */
    public Map<String,  Double> getBoxPlotData(List<Double> data) {
        double min = Collections.min(data);
        double max = Collections.max(data);
        double median = getMedian(data);
        double q1 = getMedian(data.stream().filter(i -> i < median).collect(Collectors.toList()));
        double q3 = getMedian(data.stream().filter(i -> i > median).collect(Collectors.toList()));

        Map<String, Double> boxPlotData = new HashMap<>();
        boxPlotData.put("min", min);
        boxPlotData.put("q1", q1);
        boxPlotData.put("median", median);
        boxPlotData.put("q3", q3);
        boxPlotData.put("max", max);

        return boxPlotData;
    }

    /**
     * 返回列表数据的中位数
     * @param data
     * @return
     */
    public double getMedian(List<Double> data) {
        int size = data.size();
        if (size % 2 == 0) {
            return (data.get(size / 2 - 1) + data.get(size / 2)) / 2.0;
        } else {
            return data.get(size / 2);
        }
    }

    /**
     * 返回每个模型预测误差箱形图绘制需求数据：最大值、最小值、中位数、上四分位数（Q1）、下四分位数（Q3）五个统计量
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/predictionExamination/error")
    public Map<String, Map<String, Double>> getError(@RequestParam("year") String year, @RequestParam("month") String month)
    {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Double>>() {
        }.getType();

        // 获取观察数据
        Map<String, List<Double>> monthlyComparisonData = getMonthlyComparison(year, month);
        List<Double> obsData = monthlyComparisonData.get("obs");

        // 获取预测数据
        Map<String, List<Double>> lineChartData = getLineChartData(year, month);

        // 计算每个预测模型的绝对误差并生成箱型图数据
        Map<String, Map<String, Double>> errorData = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : lineChartData.entrySet()) {
            List<Double> modelData = entry.getValue();
            List<Double> errorList = new ArrayList<>();
            for (int i = 0; i < obsData.size(); i++) {
                double error = Math.abs(obsData.get(i) - modelData.get(i));
                errorList.add(error);
            }
            Map<String, Double> boxPlotData = getBoxPlotData(errorList);
            errorData.put(entry.getKey(), boxPlotData);
        }

        return errorData;
    }
}
