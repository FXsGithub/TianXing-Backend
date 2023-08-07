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
    public Map<String, Object> getLineChartData(@RequestParam("year") String year, @RequestParam("month") String month) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Double>>() {
        }.getType();
        int currentMonthStatic = Integer.parseInt(month);
        int currentYearStatic = Integer.parseInt(year);
        //map
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> title = new HashMap<>();
        Map<String, Object> tooltip = new HashMap<>();
        Map<String, Object> legend = new HashMap<>();
        List<Object> legend_data = new ArrayList<>();
        Map<String, Object> xAxis = new HashMap<>();
        List<Object> xAxis_data = new ArrayList<>();
        Map<String, Object> xAxis_axisLabel = new HashMap<>();
        Map<String, Object> yAxis = new HashMap<>();
        List<Map<String, Object>> series= new ArrayList<>();
        Map<String, Object> series_ENSO = new HashMap<>();
        Map<String, Object> series_EnsembleForecast = new HashMap<>();
        Map<String, Object> series_ENSOCross = new HashMap<>();
        Map<String, Object> series_ENSOASC = new HashMap<>();
        Map<String, Object> series_ENSOGTC = new HashMap<>();

        String title_text="Niño 3.4 Forecast Result "+year+"-"+month;
        String title_left="center";
        String tooltip_trigger="axis";
        String legend_orient="horizontal";
        String legend_left="center";
        String legend_bottom="5";
        String legend_y="bottom";
        String grid_left="3%";
        String grid_right="4%";
        String grid_bottom="25%";
        String grid_containLabel="true";
        String xAxis_type="category";
        String xAxis_name="时间";
        int CMonth;
        if(currentMonthStatic-12==0){
            CMonth = currentMonthStatic;
        }
        else{
            CMonth=12+currentMonthStatic;
        }
        int currentYear1;
        if(currentMonthStatic==12){
            currentYear1=Integer.parseInt(year);
        }
        else{
            currentYear1=Integer.parseInt(year)-1;
        }
        int currentMonth1=CMonth-11;
        //xAxis处理
        for(int j=11;j>=0;j--){

            if(currentMonth1<13){
                xAxis_data.add(currentMonth1+"-"+currentYear1);
                currentMonth1++;
            }
            else{
                currentMonth1=1;
                currentYear1+=1;
                xAxis_data.add(currentMonth1+"-"+currentYear1);
                currentMonth1++;
            }

        }
        legend_data.add("EnsembleForecast");
        legend_data.add("ENSO-Cross");
        legend_data.add("ENSO-ASC");
        legend_data.add("ENSO-GTC");
        legend_data.add("ENSO");
        int xAxis_interval=2;
        String yAxis_type="value";
        String series_EnsembleForecast_name=("EnsembleForecast");
        String series_ENSOCross_name=("ENSO-Cross");
        String series_ENSOASC_name=("ENSO-ASC");
        String series_ENSOGTC_name=("ENSO-GTC");
        String series_ENSO_name=("ENSO");
        String series_type=("line");

        title.put("text", title_text);
        title.put("left", title_left);
        tooltip.put("trigger", tooltip_trigger);
        legend.put("y", legend_y);
        legend.put("orient",legend_orient);
        legend.put("left",legend_left);
        legend.put("bottom",legend_bottom);
        xAxis.put("type",xAxis_type);
        xAxis.put("boundaryGap",xAxis_name);
        xAxis.put("data",xAxis_data);
        xAxis.put("axisLabel",xAxis_axisLabel);
        xAxis_axisLabel.put("interval",xAxis_interval);
        yAxis.put("type",yAxis_type);
        series.add(series_ENSO);
        series.add(series_EnsembleForecast);
        series.add(series_ENSOCross);
        series.add(series_ENSOASC);
        series.add(series_ENSOGTC);
        series_ENSO.put("name",series_ENSO_name);
        series_ENSO.put("type",series_type);
        series_EnsembleForecast.put("name",series_EnsembleForecast_name);
        series_EnsembleForecast.put("type",series_type);
        series_ENSOCross.put("name",series_ENSOCross_name);
        series_ENSOCross.put("type",series_type);
        series_ENSOASC.put("name",series_ENSOASC_name);
        series_ENSOASC.put("type",series_type);
        series_ENSOGTC.put("name",series_ENSOGTC_name);
        series_ENSOGTC.put("type",series_type);



        String result1 = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_asc");  // 从数据库中查询数据
        List<Object> list1 = gson.fromJson(result1, listType);  // 将结果字符串转换为列表

        String result2 = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_gtc");
        List<Object> list2 = gson.fromJson(result2, listType);

        String result3 = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_cross");
        List<Object> list3 = gson.fromJson(result3, listType);

        String result4 = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_mc");
        List<Object> list4 = gson.fromJson(result4, listType);

        String result5 = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_mean");
        List<Object> list5 = gson.fromJson(result5, listType);
        series_ENSO.put("data",list5);
        series_EnsembleForecast.put("data",list4);
        series_ENSOCross.put("data",list3);
        series_ENSOASC.put("data",list1);
        series_ENSOGTC.put("data",list2);
        //最后的总和
        resultMap.put("title",title);
        resultMap.put("tooltip",tooltip);
        resultMap.put("legend",legend);
        resultMap.put("xAxis",xAxis);
        resultMap.put("yAxis",yAxis);
        resultMap.put("series",series);
        return resultMap;
    }

    @GetMapping("/predictionExamination/monthlyComparison")
    public Map<String, Object> getMonthlyComparison(@RequestParam("year") String year, @RequestParam("month") String month) {
        Gson gson = new Gson();

        Type listType = new TypeToken<List<Object>>() {
        }.getType();
        int currentMonthStatic = Integer.parseInt(month);
        int currentYearStatic = Integer.parseInt(year);
        String lastYear = String.valueOf(currentYearStatic-1);
        List<Object> obsData=new ArrayList<>();// 用来存放obs数据

        // 查询所有数据,今年和去年
        String YearResult2022 = ensoMapper.findObsEnsoByYear(lastYear);
        if (YearResult2022 != null) {
            List<Object> currentYearData1 = gson.fromJson(YearResult2022, listType);
            obsData.addAll(currentYearData1);
        }
        String YearResult2023 = ensoMapper.findObsEnsoByYear(year);
        if (YearResult2023 != null) {
            List<Object> currentYearData2 = gson.fromJson(YearResult2023, listType);
            obsData.addAll(currentYearData2);
        }
        int CMonth;
        if(currentMonthStatic-12==0){
            CMonth = currentMonthStatic;
        }
        else{
            CMonth=12+currentMonthStatic;
        }
        List<Object> obs_data = new ArrayList<>();
        if (CMonth-12 >0)  // 最多往前查12个月
        {
            obs_data = obsData.subList(CMonth-12,CMonth);
        }
        else{
            obs_data = obsData.subList(0, CMonth);
        }

        //map
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> title = new HashMap<>();
        Map<String, Object> tooltip = new HashMap<>();
        Map<String, Object> legend = new HashMap<>();
        Map<String, Object> legend_tooltip = new HashMap<>();
        Map<String, Object> grid = new HashMap<>();
        Map<String, Object> toolbox = new HashMap<>();
        Map<String, Object> toolbox_feature = new HashMap<>();
        Map<String, Object> toolbox_feature_saveAsImage = new HashMap<>();
        Map<String, Object> xAxis = new HashMap<>();
        List<Object> xAxis_data = new ArrayList<>();
        Map<String, Object> xAxis_axisLabel = new HashMap<>();
        Map<String, Object> yAxis = new HashMap<>();
        List<Map<String, Object>> series= new ArrayList<>();
        Map<String, Object> series_title = new HashMap<>();
        Map<String, Object> series_title_lineStyle = new HashMap<>();
        Map<String, Object> series_title_itemStyle = new HashMap<>();

        String title_text="预测结果逐月对比";
        String title_left="center";
        String tooltip_trigger="axis";
        String legend_x="center";
        String legend_y="bottom";
        String legend_tooltip_show="true";
        String grid_left="3%";
        String grid_right="4%";
        String grid_bottom="25%";
        String grid_containLabel="true";
        String xAxis_type="category";
        String xAxis_boundaryGap="false";
        int currentYear1;
        if(currentMonthStatic==12){
            currentYear1=Integer.parseInt(year);
        }
        else{
            currentYear1=Integer.parseInt(year)-1;
        }
        int currentMonth1=CMonth-11;
        //xAxis处理
        for(int j=11;j>=0;j--){

            if(currentMonth1<13){
                xAxis_data.add(currentMonth1+"-"+currentYear1);
                currentMonth1++;
            }
            else{
                currentMonth1=1;
                currentYear1+=1;
                xAxis_data.add(currentMonth1+"-"+currentYear1);
                currentMonth1++;
            }

        }
        int xAxis_interval=2;
        String yAxis_type="value";
        String yAxis_name="Niño 3.4 Index";
        String series_title_name="气候中心Nino3.4指数记录";
        String series_title_type="line";
        String series_title_lineStyle_color="black";
        int series_title_lineStyle_width=3;
        String series_title_itemStyle_color="black";

        title.put("text", title_text);
        title.put("left", title_left);
        tooltip.put("trigger", tooltip_trigger);
        legend.put("x", legend_x);
        legend.put("y", legend_y);
        legend.put("tooltip", legend_tooltip);
        legend_tooltip.put("show",legend_tooltip_show);
        grid.put("left",grid_left);
        grid.put("right",grid_right);
        grid.put("bottom",grid_bottom);
        grid.put("containLabel",grid_containLabel);
        toolbox.put("feature",toolbox_feature);
        toolbox_feature.put("saveAsImage",toolbox_feature_saveAsImage);
        xAxis.put("type",xAxis_type);
        xAxis.put("boundaryGap",xAxis_boundaryGap);
        xAxis.put("data",xAxis_data);
        xAxis.put("axisLabel",xAxis_axisLabel);
        xAxis_axisLabel.put("interval",xAxis_interval);
        yAxis.put("type",yAxis_type);
        yAxis.put("name",yAxis_name);
        series_title.put("name",series_title_name);
        series_title.put("type",series_title_type);
        series_title.put("lineStyle",series_title_lineStyle);
        series_title.put("itemStyle",series_title_itemStyle);
        series_title_lineStyle.put("color",series_title_lineStyle_color);
        series_title_lineStyle.put("width",series_title_lineStyle_width);
        series_title_itemStyle.put("color",series_title_itemStyle_color);
        series_title.put("data",obs_data);
        series.add(series_title);

        int currentYear,currentMonth;
        // 预测数据
        for (int i = 11; i >= 0; i--) {
            if (currentMonthStatic - i < 1) {
                currentYear = currentYearStatic - 1;
                currentMonth = currentMonthStatic + 12 - i;
            } else {
                currentYear = currentYearStatic;
                currentMonth = currentMonthStatic - i;
            }
            String Year = String.valueOf(currentYear);
            String Month = String.valueOf(currentMonth);
            String predictionMeanResult = ensoMapper.findEachPredictionsResultByMonthType(Year, Month, "nino34_mean");
            if (predictionMeanResult != null) {
                Map<String, Object> temp = new HashMap<>();
                String temp_name = Year + "年" + Month + "月起报预报误差";
                String temp_type = "line";
                String temp_lineStyle_type = "dashed";
                Map<String, Object> temp_lineStyle = new HashMap<>();

                temp.put("name", temp_name);
                temp.put("type", temp_type);
                temp.put("lineStyle", temp_lineStyle);
                temp_lineStyle.put("lineStyle", temp_lineStyle_type);
                if (predictionMeanResult != null) {
                    List<Object> predictionMeanList = gson.fromJson(predictionMeanResult, listType);  // 将结果字符串转换为列表
                    List<Object> predictionMeanData = predictionMeanList.subList(0, i + 1);
                    for (int j = 11; j > i; j--) {
                        predictionMeanData.add(0, "null");
                    }

                    temp.put("data", predictionMeanData);
                }
                series.add(temp);
            }
        }
        //最后的总和
        resultMap.put("title",title);
        resultMap.put("tooltip",tooltip);
        resultMap.put("legend",legend);
        resultMap.put("grid",grid);
        resultMap.put("toolbox",toolbox);
        resultMap.put("xAxis",xAxis);
        resultMap.put("yAxis",yAxis);
        resultMap.put("series",series);

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
    public List<Object> getError(@RequestParam("year") String year, @RequestParam("month") String month) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Double>>() {
        }.getType();
        int currentMonthStatic = Integer.parseInt(month);
        int currentYearStatic = Integer.parseInt(year);
        String lastYear = String.valueOf(currentYearStatic-1);

        List<Object> obsData=new ArrayList<>();// 用来存放obs数据

        // 查询所有数据,今年和去年
        String YearResult2022 = ensoMapper.findObsEnsoByYear(lastYear);
        if (YearResult2022 != null) {
            List<Object> currentYearData1 = gson.fromJson(YearResult2022, listType);
            obsData.addAll(currentYearData1);
        }
        String YearResult2023 = ensoMapper.findObsEnsoByYear(year);
        if (YearResult2023 != null) {
            List<Object> currentYearData2 = gson.fromJson(YearResult2023, listType);
            obsData.addAll(currentYearData2);
        }
        int CMonth;
        if(currentMonthStatic-12==0){
            CMonth = currentMonthStatic;
        }
        else{
            CMonth=12+currentMonthStatic;
        }
        List<Object> obs_data = new ArrayList<>();
        if (CMonth-12 >0)  // 最多往前查12个月
        {
            obs_data = obsData.subList(CMonth-12,CMonth);
        }
        else{
            obs_data = obsData.subList(0, CMonth);
        }
        //map
        List<Object> option=new ArrayList<>();// 用来存放obs数据
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> title = new HashMap<>();
        Map<String, Object> tooltip = new HashMap<>();
        Map<String, Object> tooltip_axisPointer = new HashMap<>();
        Map<String, Object> tooltip_axisPointer_crossStyle = new HashMap<>();
        Map<String, Object> legend = new HashMap<>();
        Map<String, Object> legend_tooltip = new HashMap<>();
        Map<String, Object> toolbox = new HashMap<>();
        Map<String, Object> toolbox_feature = new HashMap<>();
        Map<String, Object> toolbox_feature_saveAsImage = new HashMap<>();
        Map<String,Object> xAxis= new HashMap<>();
        List<Object> xAxis1 = new ArrayList<>();
        List<Object> xAxis_data = new ArrayList<>();
        Map<String, Object> xAxis_axisLabel = new HashMap<>();
        Map<String, Object> xAxis_axisPointer = new HashMap<>();
        Map<String,Object> yAxis= new HashMap<>();
        List<Object> yAxis1 = new ArrayList<>();
        List<Map<String, Object>> series= new ArrayList<>();

        String title_text="预报结果误差柱状图";
        String title_left="center";
        String tooltip_trigger="axis";
        String tooltip_axisPointer_type="cross";
        String tooltip_axisPointer_crossStyle_color="#999";
        String legend_x="center";
        String legend_y="bottom";
        String legend_tooltip_show="true";
        String xAxis_type="category";
        String xAxis_axisPointer_type="shadow";
        int xAxis_interval=2;
        String yAxis_type="value";
        String yAxis_name="Niño 3.4 Index";


        int currentYear1;
        if(currentMonthStatic==12){
            currentYear1=Integer.parseInt(year);
        }
        else{
            currentYear1=Integer.parseInt(year)-1;
        }
        int currentMonth1=CMonth-11;
        //xAxis处理
        for(int j=11;j>=0;j--){

            if(currentMonth1<13){
                xAxis_data.add(currentMonth1+"-"+currentYear1);
                currentMonth1++;
            }
            else{
                currentMonth1=1;
                currentYear1+=1;
                xAxis_data.add(currentMonth1+"-"+currentYear1);
                currentMonth1++;
            }

        }

        int currentYear,currentMonth;
        // 预测数据
        for (int i = 11; i >= 0; i--) {
            if (currentMonthStatic - i < 1) {
                currentYear = currentYearStatic - 1;
                currentMonth = currentMonthStatic + 12 - i;
            } else {
                currentYear = currentYearStatic;
                currentMonth = currentMonthStatic - i;
            }
            String Year = String.valueOf(currentYear);
            String Month = String.valueOf(currentMonth);
            String predictionMeanResult = ensoMapper.findEachPredictionsResultByMonthType(Year, Month, "nino34_mean");
            if (predictionMeanResult != null) {
                Map<String, Object> temp = new HashMap<>();
                Map<String, Object> tempObs = new HashMap<>();
                Map<String, Object> tempError = new HashMap<>();

                title.put("text", title_text);
                title.put("left", title_left);
                tooltip.put("trigger", tooltip_trigger);
                tooltip.put("axisPointer", tooltip_axisPointer);
                tooltip_axisPointer.put("type", tooltip_axisPointer_type);
                tooltip_axisPointer.put("crossStyle", tooltip_axisPointer_crossStyle);
                tooltip_axisPointer_crossStyle.put("color", tooltip_axisPointer_crossStyle_color);
                legend.put("x", legend_x);
                legend.put("y", legend_y);
                legend.put("tooltip", legend_tooltip);
                legend_tooltip.put("show", legend_tooltip_show);
                toolbox.put("feature", toolbox_feature);
                toolbox_feature.put("saveAsImage", toolbox_feature_saveAsImage);
                xAxis.put("type", xAxis_type);
                xAxis.put("axisPointer", xAxis_axisPointer);
                xAxis_axisPointer.put("type", xAxis_axisPointer_type);
                xAxis_data.remove(0);//每次删去一个
                xAxis.put("data", xAxis_data);
                xAxis.put("axisLabel", xAxis_axisLabel);
                xAxis_axisLabel.put("interval", xAxis_interval);
                yAxis.put("type", yAxis_type);
                yAxis.put("name", yAxis_name);

                //误差

                String temp_name = Year + "年" + Month + "月起报预报误差";
                String temp_type = "line";
                String temp_lineStyle_type = "dashed";
                Map<String, Object> temp_lineStyle = new HashMap<>();

                temp.put("name", temp_name);
                temp.put("type", temp_type);
                temp.put("lineStyle", temp_lineStyle);
                temp_lineStyle.put("lineStyle", temp_lineStyle_type);
                List<Object> predictionMeanData = new ArrayList<>();

                List<Object> predictionMeanList = gson.fromJson(predictionMeanResult, listType);  // 将结果字符串转换为列表
                predictionMeanData = predictionMeanList.subList(0, i + 1);
                ;
                temp.put("data", predictionMeanData);

                series.add(temp);
                //标准
                Map<String, Object> tempObs_lineStyle = new HashMap<>();
                List<Object> obs_data_fin = obs_data.subList(11 - i, obs_data.size());
                String tempObs_name = "气候中心Nino3.4指数记录";
                String tempObs_type = "line";
                String tempObs_lineStyle_type = "dashed";
                tempObs_lineStyle.put("type", tempObs_lineStyle_type);
                tempObs.put("name", tempObs_name);
                tempObs.put("type", tempObs_type);
                tempObs.put("lineStyle", tempObs_lineStyle);
                tempObs.put("data", obs_data_fin);
                series.add(tempObs);
                //差集
                List<Object> error = new ArrayList<>();
                for (int j = 0; j <= i; j++) {
                    //？？？
                    error.add(Math.abs((Double.parseDouble(predictionMeanData.get(j).toString()) - Double.parseDouble(obs_data_fin.get(j).toString()))));
                }
                String tempError_name = Year + "年" + Month + "月起报";
                tempError.put("name", tempError_name);
                tempError.put("type", tempObs_type);
                tempError.put("lineStyle", tempObs_lineStyle);
                tempError.put("data", error);
                series.add(tempError);

            }
        }
        //最后的总和
        resultMap.put("title",title);
        resultMap.put("tooltip",tooltip);
        resultMap.put("legend",legend);
        resultMap.put("toolbox",toolbox);
        xAxis1.add(xAxis);
        yAxis1.add(yAxis);
        resultMap.put("xAxis",xAxis1);
        resultMap.put("yAxis",yAxis1);
        resultMap.put("series",series);
        option.add(resultMap);
        return option;
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
//    @GetMapping("/predictionExamination/errorBox")
//    public Map<String, Map<String, Double>> getErrorBox(@RequestParam("year") String year, @RequestParam("month") String month) {
//        Gson gson = new Gson();
//        Type listType = new TypeToken<List<Double>>() {
//        }.getType();
//        Map<String, Object> resultmap;
//        Map<String, Object> errormap;
//
//        //map
//        Map<String, Object> resultMap = new HashMap<>();
//        Map<String, Object> title = new HashMap<>();
//        Map<String, Object> tooltip = new HashMap<>();
//        Map<String, Object> tooltip_axisPointer = new HashMap<>();
//        Map<String, Object> legend = new HashMap<>();
//        Map<String, Object> legend_tooltip = new HashMap<>();
//        Map<String, Object> grid = new HashMap<>();
//        Map<String, Object> toolbox = new HashMap<>();
//        Map<String, Object> toolbox_feature = new HashMap<>();
//        Map<String, Object> toolbox_feature_saveAsImage = new HashMap<>();
//        Map<String, Object> xAxis = new HashMap<>();
//        List<Object> xAxis_data = new ArrayList<>();
//        Map<String, Object> xAxis_axisLabel = new HashMap<>();
//        Map<String, Object> yAxis = new HashMap<>();
//        List<Map<String, Object>> series= new ArrayList<>();
//        Map<String, Object> series_title = new HashMap<>();
//        Map<String, Object> series_title_lineStyle = new HashMap<>();
//        Map<String, Object> series_title_itemStyle = new HashMap<>();
//
//        String title_text="误差分析箱型图";
//        String title_left="center";
//        String tooltip_axisPointer_type="shadow";
//        String tooltip_trigger="item";
//        String legend_x="center";
//        String legend_y="bottom";
//        String legend_tooltip_show="true";
//        String grid_left="3%";
//        String grid_right="4%";
//        String grid_bottom="25%";
//        String grid_containLabel="true";
//        String xAxis_type="category";
//        String xAxis_boundaryGap="false";
//        xAxis_data.add("Feb-22");
//        xAxis_data.add("Mar-22");
//        xAxis_data.add("Apr-22");
//        xAxis_data.add("Jun-22");
//        xAxis_data.add("Jul-22");
//        xAxis_data.add("Aug-22");
//        xAxis_data.add("Sep-22");
//        xAxis_data.add("Oct-22");
//        xAxis_data.add("Nov-22");
//        xAxis_data.add("Dec-22");
//        xAxis_data.add("Jan-22");
//        int xAxis_interval=2;
//        String yAxis_type="value";
//        String yAxis_name="Niño 3.4 Index";
//        String series_title_name="气候中心Nino3.4指数记录";
//        String series_title_type="line";
//        String series_title_lineStyle_color="black";
//        int series_title_lineStyle_width=3;
//        String series_title_itemStyle_color="black";
//
//        title.put("text", title_text);
//        title.put("left", title_left);
//        tooltip.put("trigger", tooltip_trigger);
//        legend.put("x", legend_x);
//        legend.put("y", legend_y);
//        legend.put("tooltip", legend_tooltip);
//        legend_tooltip.put("show",legend_tooltip_show);
//        grid.put("left",grid_left);
//        grid.put("right",grid_right);
//        grid.put("bottom",grid_bottom);
//        tooltip.put("trigger",tooltip_trigger);
//        tooltip.put("axisPointer",tooltip_axisPointer);
//        tooltip_axisPointer.put("type",tooltip_axisPointer_type);
//        toolbox.put("feature",toolbox_feature);
//        toolbox_feature.put("saveAsImage",toolbox_feature_saveAsImage);
//        xAxis.put("type",xAxis_type);
//        xAxis.put("boundaryGap",xAxis_boundaryGap);
//        xAxis.put("data",xAxis_data);
//        xAxis.put("axisLabel",xAxis_axisLabel);
//        xAxis_axisLabel.put("interval",xAxis_interval);
//        yAxis.put("type",yAxis_type);
//        yAxis.put("name",yAxis_name);
//        series_title.put("name",series_title_name);
//        series_title.put("type",series_title_type);
//        series_title.put("lineStyle",series_title_lineStyle);
//        series_title.put("itemStyle",series_title_itemStyle);
//        series_title_lineStyle.put("color",series_title_lineStyle_color);
//        series_title_lineStyle.put("width",series_title_lineStyle_width);
//        series_title_itemStyle.put("color",series_title_itemStyle_color);
//        series_title.put("data",obs_data);
//        series.add(series_title);
//        errormap.put("option",resultmap);
//        // 获取观察数据
//        Map<String, List<Object>> monthlyComparisonData = getMonthlyComparison(year, month);
//        List<Object> obs = monthlyComparisonData.get("obs");
//        List<Double> obsData = (List<Double>)(List)obs;
//
//        // 获取预测数据
//        String predictionMeanResult = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_mean");
//        List<Double> predictionMeanList = gson.fromJson(predictionMeanResult, listType);
//        List<Double> predictionMeanData = predictionMeanList.subList(0, obsData.size());
//
//        List<Double> errorList = new ArrayList<>();
//        for (int i = 0; i < obsData.size(); i++) {
//            double error = Math.abs(obsData.get(i) - predictionMeanData.get(i));
//            errorList.add(error);
//        }
//        Map<String, Double> boxPlotData = getBoxPlotData(errorList);
//
//        Map<String, Map<String, Double>> errorBoxMap = new HashMap<>();
//        errorBoxMap.put("nino34_mean", boxPlotData);
//        return errorBoxMap;
//    }

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
     * 返回 year month 当月 mean 模型预测数据和真实数据的皮尔逊相关系数
     *
     * @param year
     * @param month
     * @return
     */
//    @GetMapping("/predictionExamination/errorCorr")
//    public Map<String, Double> getErrorCorr(@RequestParam("year") String year, @RequestParam("month") String month) {
//        // 创建 Gson 对象和类型对象以解析 JSON
//        Gson gson = new Gson();
//        Type listType = new TypeToken<List<Double>>() {
//        }.getType();
//
//        // 获取观察数据
//        Map<String, List<Object>> monthlyComparisonData = getMonthlyComparison(year, month);
//        List<Object> obs = monthlyComparisonData.get("obs");
//        List<Double> obsData = (List<Double>)(List)obs;
//
//        // 获取预测数据
//        String predictionMeanResult = ensoMapper.findEachPredictionsResultByMonthType(year, month, "nino34_mean");
//        List<Double> predictionMeanList = gson.fromJson(predictionMeanResult, listType);
//        List<Double> predictionMeanData = predictionMeanList.subList(0, obsData.size());
//
//        // 计算皮尔逊相关系数
//        double correlation = getPearsonCorrelation(obsData, predictionMeanData);
//
//        // 创建一个哈希映射以存储皮尔逊相关系数
//        Map<String, Double> correlationMap = new HashMap<>();
//        correlationMap.put("nino34_mean", correlation);
//
//        return correlationMap;
//    }
//
}