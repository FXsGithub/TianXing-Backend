package com.tongji.enso.mybatisdemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongji.enso.mybatisdemo.entity.online.Info_sic_latlon;
import com.tongji.enso.mybatisdemo.entity.online.Obs_nao;
import com.tongji.enso.mybatisdemo.entity.online.Tj_nao;
import com.tongji.enso.mybatisdemo.entity.online.Tj_sic;
import com.tongji.enso.mybatisdemo.service.online.Info_sic_latlonService;
import com.tongji.enso.mybatisdemo.service.online.Obs_naoService;
import com.tongji.enso.mybatisdemo.service.online.Tj_naoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/nao")
public class Tj_naoController {

    @Autowired
    private Tj_naoService tj_naoservice;
    @Autowired
    private Obs_naoService obs_naoservice;
    @Autowired
    private Info_sic_latlonService info_sic_latlonService;

    /**
     * 查询某月份的NAO指数数据
     * @param: year, month;
     * @return: Map<String, Object>.
     */
    @GetMapping("/predictionResult/nao")
    @ApiOperation(notes = "查询月份开始六个月的NAO指数预测和观测结果以及文本描述", value = "根据月份查询NAO指数预测和观测结果")
    public Map<String,Object> findNAOPredictionByMonthAndModel(@RequestParam String year, @RequestParam String month){

        Tj_nao preResult = tj_naoservice.findPredictionByMonthAndModel(year,month);
        // 数据库中观测数据按年存储，所以查询月份都是1，查出来的是12个月份的数据
        Obs_nao obsResult = obs_naoservice.findObservationByMonthAndModel(year,"1");

        // 将month从String转为int型整数
        int m = Integer.parseInt(month);

        Map<String,Object> naoMap = new HashMap<>();

        double []pre_data = new double[6];
        double []obs_data = new double[6];
        // data[]用于临时存放转化后的原始观测数据
        double []data = null;

        String jsonString;
        ObjectMapper objectMapper=new ObjectMapper();

        try{
            // 将json形式的预测数据转化为一维数组
            jsonString = preResult.getData();
            pre_data = objectMapper.readValue(jsonString,double[].class);

            // 对于观测数据，需要根据查询月份是否大于7分两类情况处理
            // 首先将当前查询年份的观测数据转化为一维数组
            jsonString= obsResult.getData();
            data = objectMapper.readValue(jsonString,double[].class);

            // 如果查询月份大于7，则需要用到下一年的数据
            if(m > 7){
                int n = m - 7;  // n表示要用到下一年的前n个数据
                int i;
                int j = 13 - m;
                // 把该年从查询月份开始之后的数据放入obs_data[]中
                for(i = 0; i < j; i++){
                    obs_data[i] = data[m-1];
                    m++;
                }

                // 将年份从String转为int型整数，+1后再转回String型用于查询下一年的数据
                int y =Integer.parseInt(year);
                y++;
                String nextYear=Integer.toString(y);
                Obs_nao obsResult_nextYear = obs_naoservice.findObservationByMonthAndModel(nextYear,"1");
                // 将下一年的观测数据转化为一维数组
                jsonString = obsResult_nextYear.getData();
                data = objectMapper.readValue(jsonString,double[].class);

                // 把下一年前n月的数据放入obs_data[]中
                for(j = 0; j < n; j++){
                    obs_data[i] = data[j];
                    i++;
                }
            }
            // 如果查询月份小于等于7，则直接从该月份开始将数据逐个放入obs_data[]中
            else{
                for(int i = 0; i < 6; i++){
                    obs_data[i] = data[m-1];
                    m++;
                }
            }
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        naoMap.put("predictionData", pre_data);
        naoMap.put("observationData", obs_data);

        return naoMap;
    }

    /**
     * 根据年月，返回该时模型起报的6个月格点数据
     * @param: year, month;
     * @return: Map<String, Object>.
     */
     @GetMapping("/findGridData/nao")
     @ApiOperation(notes = "根据年月，返回该时模型起报的6个月格点数据。" +
             "返回为data1~data6数据，以及lat和lon，均以二维数组呈现", value = "根据年月返回格点数据")
     public Map<String,Object> findGridByMonth(@RequestParam String year, @RequestParam String month) {

         Tj_nao nao = tj_naoservice.findGridByMonth(year,month);
         Info_sic_latlon latlon = info_sic_latlonService.findlatlon();
         Map<String, Object> GridMap=new HashMap<>();
         ObjectMapper objectMapper = new ObjectMapper();

         double[][][] data = null;
         // 将data字段转化为三维数组
         try {
             String jsonString = nao.getData();
             data = objectMapper.readValue(jsonString, double[][][].class);
         } catch (JsonProcessingException e) {
             e.printStackTrace();
         }
         //将6个月的数据分别返回
         for(int i=0;i<6;i++){
             GridMap.put("data"+ (i + 1),data[i]);
         }
         //用二维数组表示纬度
         double[][] lat=null;
         try {
             String jsonString = latlon.getLat();
             lat = objectMapper.readValue(jsonString, double[][].class);
         } catch (JsonProcessingException e) {
             e.printStackTrace();
         }
         GridMap.put("lat",lat);
         //用二维数组表示经度
         double[][] lon=null;
         try {
             String jsonString = latlon.getLon();
             lon = objectMapper.readValue(jsonString, double[][].class);
         } catch (JsonProcessingException e) {
             e.printStackTrace();
         }
         GridMap.put("lon",lon);
         return GridMap;
     }
}
