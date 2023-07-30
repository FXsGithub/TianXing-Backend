package com.tongji.enso.mybatisdemo.controller;

import com.tongji.enso.mybatisdemo.mapper.online.EnsoMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;


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
     *
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
        List<List<List<Double>>> resultList = gson.fromJson(result, new TypeToken<List<List<List<Double>>>>() {
        }.getType());

        // 将结果添加到 Map 中并返回
        Map<String, List<List<Double>>> resultMap = new HashMap<>();
        if (monthIndex >= 0 && monthIndex < resultList.size()) {
            resultMap.put("ssta_mean", resultList.get(monthIndex));
        }

        return resultMap;
    }

    /**
     * 根据指定的年份和月份，返回该月的观测数据和预测数据
     * 根据观测数据的最新时间，决定返回数据的长度，至多18个数据
     *
     * @param year
     * @param month
     * @return
     */
//    @GetMapping("/predictionExamination/monthlyComparison")
//    public Map<String, List<Double>> getMonthlyComparison(@RequestParam("year") String year, @RequestParam("month") String month) {
//        Gson gson = new Gson();
//        Type listType = new TypeToken<List<Double>>() {
//        }.getType();
//        // 使用给定年份查询所有数据
//        String YearResult2022 = ensoMapper.findObsEnsoByYear("2022");
//        String YearResult2023 = ensoMapper.findObsEnsoByYear("2023");
//        String currentYearResult = YearResult2022+ YearResult2023;
//        List<Double> currentYearData = gson.fromJson(currentYearResult, listType);
//
//        List<Double> current18MonthsData;  // 用来存放从本月起（包括本月）未来18个月的数据，直到真实数据用完为止
//
//        current18MonthsData = currentYearData.subList(Integer.parseInt(month) - 1, currentYearData.size());
//
//        if (currentYearData.size() == 12)  // 需要考虑下一年的真实数据
//        {
//            // 查询下一年的数据
//            String futureYear = String.valueOf(Integer.parseInt(year) + 1);
//            String futureYearResult = ensoMapper.findObsEnsoByYear(futureYear);
//
//            if (futureYearResult != null) {
//                List<Double> futureYearData = gson.fromJson(futureYearResult, listType);
//                current18MonthsData.addAll(futureYearData);
//                if (futureYearData.size() == 12 && current18MonthsData.size() < 18) {
//                    // 查询下一年的下一年的数据
//                    String futureFutureYear = String.valueOf(Integer.parseInt(year) + 2);
//                    String futureFutureYearResult = ensoMapper.findObsEnsoByYear(futureFutureYear);
//                    if (futureFutureYearResult != null) {
//                        List<Double> futureFutureYearData = gson.fromJson(futureFutureYearResult, listType);
//                        current18MonthsData.addAll(futureFutureYearData);
//                    }
//                }
//            }
//        }
//
//        if (current18MonthsData.size() > 18)  // 超过18个月的数据，只取前18个月
//            current18MonthsData = current18MonthsData.subList(0, 18);
//
//
//        Map<String, List<Double>> resultMap = new HashMap<>();
//        Map<String, List<Object>> resultMap2 = new HashMap<>();
//        String name = year + "年" + month + "月起报预报误差";
//        List<Object> nameList = new ArrayList<>();
//        nameList.add(name);
//        List<Object> typeList = new ArrayList<>();
//        typeList.add("line");
//
//        List<Object> lineStyleList = new ArrayList<>();
//        lineStyleList.add(null);
//
//        resultMap2.put("name", nameList);
//        resultMap2.put("type", typeList);
//        resultMap2.put("lineStyle", lineStyleList);
//
//        resultMap.put("obs", current18MonthsData);
//
//
//        // 获取预测数据
//        String predictionMeanResult = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_mean");
//        List<Double> predictionMeanList = gson.fromJson(predictionMeanResult, listType);  // 将结果字符串转换为列表
//        // 只需前 current18MonthsData.size() 个数据
//        List<Double> predictionMeanData = predictionMeanList.subList(0, current18MonthsData.size());
//        resultMap.put("mean", predictionMeanData);
//
//        return resultMap;
//    }

    @GetMapping("/predictionExamination/monthlyComparison")
    public Map<String, List<Double>> getMonthlyComparison(@RequestParam("year") String year, @RequestParam("month") String month) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Double>>() {
        }.getType();

        // 查询所有数据(之后要改）
        String YearResult2022 = ensoMapper.findObsEnsoByYear("2022");
        List<Double> currentYearData1 = gson.fromJson(YearResult2022, listType);

        String YearResult2023 = ensoMapper.findObsEnsoByYear("2023");
        List<Double> currentYearData2 = gson.fromJson(YearResult2023, listType);
        List<Double> allYearData=new ArrayList<>();
        allYearData.addAll(currentYearData1);
        allYearData.addAll(currentYearData2);
        List<Double> obsData;  // 用来存放obs数据
        obsData = allYearData.subList(0 , allYearData.size());
        // 使用给定年份查询所有数据
        String CurrentYearResult = ensoMapper.findObsEnsoByYear(year);
        List<Double> currentYearData = gson.fromJson(CurrentYearResult, listType);

        List<Double> current18MonthsData;  // 用来存放从本月起（包括本月）未来18个月的数据，直到真实数据用完为止


        current18MonthsData = currentYearData.subList(Integer.parseInt(month) , currentYearData.size());


        if (currentYearData.size() == 12)  // 需要考虑下一年的真实数据
        {
            // 查询下一年的数据
            String futureYear = String.valueOf(Integer.parseInt(year) + 1);
            String futureYearResult = ensoMapper.findObsEnsoByYear(futureYear);

            if (futureYearResult != null) {
                List<Double> futureYearData = gson.fromJson(futureYearResult, listType);
                current18MonthsData.addAll(futureYearData);
                if (futureYearData.size() == 12 && current18MonthsData.size() < 18) {
                    // 查询下一年的下一年的数据
                    String futureFutureYear = String.valueOf(Integer.parseInt(year) + 2);
                    String futureFutureYearResult = ensoMapper.findObsEnsoByYear(futureFutureYear);
                    if (futureFutureYearResult != null) {
                        List<Double> futureFutureYearData = gson.fromJson(futureFutureYearResult, listType);
                        current18MonthsData.addAll(futureFutureYearData);
                    }
                }
            }
        }

        if (current18MonthsData.size() > 18)  // 超过18个月的数据，只取前18个月
            current18MonthsData = current18MonthsData.subList(0, 18);


        Map<String, List<Double>> resultMap = new HashMap<>();
        Map<String, List<Object>> resultMap2 = new HashMap<>();
        String name = year + "年" + month + "月起报预报误差";
        List<Object> nameList = new ArrayList<>();
        nameList.add(name);
        List<Object> typeList = new ArrayList<>();
        typeList.add("line");

        List<Object> lineStyleList = new ArrayList<>();
        lineStyleList.add(null);

        resultMap2.put("name", nameList);
        resultMap2.put("type", typeList);
        resultMap2.put("lineStyle", lineStyleList);

        resultMap.put("obs", obsData);


        // 获取预测数据
        String predictionMeanResult = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_mean");
        List<Double> predictionMeanList = gson.fromJson(predictionMeanResult, listType);  // 将结果字符串转换为列表
        // 只需前 current18MonthsData.size() 个数据
        List<Double> predictionMeanData = predictionMeanList.subList(Integer.parseInt(month) , predictionMeanList.size());
        resultMap.put("mean", predictionMeanData);

        return resultMap;
    }


    /**
     * 计算预测模型 nino34_mean 与观测数据在指定年月的绝对误差
     *
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/predictionExamination/error")
    public Map<String, List<Double>> getError(@RequestParam("year") String year, @RequestParam("month") String month) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Double>>() {
        }.getType();

        // 获取观察数据
        Map<String, List<Double>> monthlyComparisonData = getMonthlyComparison(year, month);
        List<Double> obsData = monthlyComparisonData.get("obs");

        // 获取预测数据
        String predictionMeanResult = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_mean");
        List<Double> predictionMeanList = gson.fromJson(predictionMeanResult, listType);
        List<Double> predictionMeanData = predictionMeanList.subList(0, obsData.size());

        List<Double> errorList = new ArrayList<>();
        for (int i = 0; i < obsData.size(); i++) {
            double error = Math.abs(obsData.get(i) - predictionMeanData.get(i));
            errorList.add(error);
        }

        Map<String, List<Double>> errorMap = new HashMap<>();
        errorMap.put("nino34_mean", errorList);

        return errorMap;
    }

    /**
     * 返回数据列表的箱型图数据（最小值、Q1、中位数、Q3、最大值）
     *
     * @param data
     * @return
     */
    private Map<String, Double> getBoxPlotData(List<Double> data) {
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
     *
     * @param data
     * @return
     */
    private double getMedian(List<Double> data) {
        int size = data.size();
        if (size % 2 == 0) {
            return (data.get(size / 2 - 1) + data.get(size / 2)) / 2.0;
        } else {
            return data.get(size / 2);
        }
    }

    /**
     * 预测模型 nino34_mean 预测误差箱形图绘制需求数据：最大值、最小值、中位数、上四分位数（Q1）、下四分位数（Q3）五个统计量
     *
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/predictionExamination/errorBox")
    public Map<String, Map<String, Double>> getErrorBox(@RequestParam("year") String year, @RequestParam("month") String month) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Double>>() {
        }.getType();

        // 获取观察数据
        Map<String, List<Double>> monthlyComparisonData = getMonthlyComparison(year, month);
        List<Double> obsData = monthlyComparisonData.get("obs");

        // 获取预测数据
        String predictionMeanResult = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_mean");
        List<Double> predictionMeanList = gson.fromJson(predictionMeanResult, listType);
        List<Double> predictionMeanData = predictionMeanList.subList(0, obsData.size());

        List<Double> errorList = new ArrayList<>();
        for (int i = 0; i < obsData.size(); i++) {
            double error = Math.abs(obsData.get(i) - predictionMeanData.get(i));
            errorList.add(error);
        }
        Map<String, Double> boxPlotData = getBoxPlotData(errorList);

        Map<String, Map<String, Double>> errorBoxMap = new HashMap<>();
        errorBoxMap.put("nino34_mean", boxPlotData);
        return errorBoxMap;
    }

    /**
     * 根据提供的观测值和预测值计算皮尔逊相关系数
     *
     * @param observed
     * @param predicted
     * @return
     */
    private double getPearsonCorrelation(List<Double> observed, List<Double> predicted) {
        double[] obsArray = observed.stream().mapToDouble(d -> d).toArray();
        double[] predArray = predicted.stream().mapToDouble(d -> d).toArray();

        PearsonsCorrelation correlation = new PearsonsCorrelation();
        return correlation.correlation(obsArray, predArray);
    }

    /**
     * 返回 year month 当月 mean模型预测数据和真实数据的皮尔逊相关系数
     *
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/predictionExamination/errorCorr")
    public Map<String, Double> getErrorCorr(@RequestParam("year") String year, @RequestParam("month") String month) {
        // 创建 Gson 对象和类型对象以解析 JSON
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Double>>() {
        }.getType();

        // 获取观察数据
        Map<String, List<Double>> monthlyComparisonData = getMonthlyComparison(year, month);
        List<Double> obsData = monthlyComparisonData.get("obs");

        // 获取预测数据
        String predictionMeanResult = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_mean");
        List<Double> predictionMeanList = gson.fromJson(predictionMeanResult, listType);
        List<Double> predictionMeanData = predictionMeanList.subList(0, obsData.size());

        // 计算皮尔逊相关系数
        double correlation = getPearsonCorrelation(obsData, predictionMeanData);

        // 创建一个哈希映射以存储皮尔逊相关系数
        Map<String, Double> correlationMap = new HashMap<>();
        correlationMap.put("nino34_mean", correlation);

        return correlationMap;
    }
}

