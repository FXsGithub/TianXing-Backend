package com.tongji.enso.mybatisdemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongji.enso.mybatisdemo.entity.online.Info_sic_latlon;
import com.tongji.enso.mybatisdemo.entity.online.Tj_sic;
import com.tongji.enso.mybatisdemo.service.online.Info_sic_latlonService;
import com.tongji.enso.mybatisdemo.service.online.Tj_sicService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/seaice")
public class Tj_sicController {

    @Autowired
    private Tj_sicService tj_sicservice;
    @Autowired
    private Info_sic_latlonService info_sic_latlonService;

    /**
     * 查询全部的SIC指数
     */
    @GetMapping("/findAllSIC")
    @ApiOperation(notes = "查询全部Sic指数，返回'Tj_sic'类型列表", value = "查询全部Sic指数")
    public List<Tj_sic> findAll(){ return tj_sicservice.findAllSIC(); }

    /**
     * 查询某月份的SIC指数
     * @param: year, month, day;
     * @return: Map<String, Object>.
     */
    @GetMapping("/predictionResult/SIC")
    @ApiOperation(notes = "查询指定日期所有经纬度的SIC指数，坐标以及文本描述,返回的trans_data,x_axis,y_axis均为二维数组，通过" +
            "格点确定数据和横纵坐标，如trans_data[1][1]表示格点（1,1）处的数据，" +
            "x_axis[1][1]表示格点(1,1)处的横坐标", value = "根据日期查询SIC指数预测结果")
    public Map<String, Object> findSICPredictionByDate(@RequestParam String year,@RequestParam String month,@RequestParam String day){

        Tj_sic sic = tj_sicservice.findPredictionByDate(year,month,day);
        Info_sic_latlon latlon = info_sic_latlonService.findlatlon();
        Map<String, Object> sicMap=new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        double[][][] data = null;
        // 将data字段转化为三维数组
        try {
            String jsonString = sic.getData();
            data = objectMapper.readValue(jsonString, double[][][].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //用二维数组表示该日的sic指数预测结果
        double[][] trans_data=data[0];
        sicMap.put("trans_data",trans_data);

        //用二维数组表示纬度
        double[][] lat=null;
        try {
            String jsonString = latlon.getLat();
            lat = objectMapper.readValue(jsonString, double[][].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //用二维数组表示经度
        double[][] lon=null;
        try {
            String jsonString = latlon.getLon();
            lon = objectMapper.readValue(jsonString, double[][].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //将经纬纬度数据转化为一维平面直角坐标系坐标,设半径为100
        double[][] x_axis=lat;
        double[][] y_axis=lon;
        // 获取二维数组的行数
        int rows = lat.length;

        // 使用循环遍历二维数组
        for (int i = 0; i < rows; i++) {
            // 获取当前行的列数
            int cols = lat[i].length;
            // 使用内层循环遍历当前行的所有元素
            for (int j = 0; j < cols; j++) {
                double r=100*(90-lat[i][j])/90;//距离原点距离
                x_axis[i][j]= Math.cos((lon[i][j]-90)/180*2*Math.PI)*r;
                y_axis[i][j]= Math.sin((lon[i][j]-90)/180*2*Math.PI)*r;
            }
        }
        sicMap.put("x_axis",x_axis);
        sicMap.put("y_axis",y_axis);
        return sicMap;
    }

    @GetMapping("/error")
    @ApiOperation(notes = "查询月份4周的SIC预测结果与基线方法的比较以及文本描述", value = "根据月份查询SIC指数预测结果与基线方法的比较")
    public Map<String, Object> findSICErrorByMonth(@RequestParam String year,@RequestParam String month){

        List<Tj_sic> sicList=tj_sicservice.findErrorByMonth(year,month);

        Map<String, Object> sicMap=new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        double []data = null;
        for(Tj_sic sic:sicList){
            try {
                String jsonString = sic.getData();
                data = objectMapper.readValue(jsonString, double[].class);
                sicMap.put(sic.getVar_model(),data);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return sicMap;
    }

    @GetMapping("/errorBox")
    @ApiOperation(notes="查询年份四种SIC预测结果提前1到7天的统计结果误差箱型图数据，文本描述",value="查询sic误差箱型图数据")
    public Map<String, Object> findSICErrorBoxByYear(@RequestParam String year){

        List<Tj_sic> sicList=tj_sicservice.findErrorBoxByYearAndModel(year);

        Map<String, Object> sicMap=new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();


        double []data = null;

        // 遍历查询结果中的每组数据
        for(Tj_sic sic:sicList){
            try {
                String jsonString = sic.getData();
                data = objectMapper.readValue(jsonString, double[].class);  // 将data字段转为一维数组
                int size= data.length / 7;  // size表示数据等分为7组后，每组数据的长度

                // 创建二维数组result存储最终结果，result[0][]表示提前1天的统计结果误差，以此类推
                double [][]result = new double[7][size];
                int j = 0;
                for(int i = 0; i < data.length; i++){
                    result[i % 7][j] = data[i];
                    if (i % 7 == 6) {   // 每取7个数据，j++
                        j++;
                    }
                }
                // 将result放入map中
                sicMap.put(sic.getVar_model(),result);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return sicMap;
    }

}
