package com.tongji.enso.mybatisdemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongji.enso.mybatisdemo.entity.online.*;
import com.tongji.enso.mybatisdemo.mapper.online.ImgsMapper;
import com.tongji.enso.mybatisdemo.service.online.ImgsService;
import com.tongji.enso.mybatisdemo.service.online.Info_sic_latlonService;
import com.tongji.enso.mybatisdemo.service.online.Obs_naoService;
import com.tongji.enso.mybatisdemo.service.online.Tj_naoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.dialect.HsqlDbDialect;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/nao")
public class Tj_naoController {

    @Autowired
    private Tj_naoService tj_naoservice;
    @Autowired
    private Obs_naoService obs_naoservice;
    @Autowired
    private ImgsService imgsservice;

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
     * 根据年月，返回该时模型起报的6个月格点数据的模块图
     * @param: year, month;
     * @return: List<String>.
     */
     @GetMapping("/findGridData/nao")
     @ApiOperation(notes = "根据年月，返回该时模型起报的6个月格点数据的模块图", value = "根据年月返回格点数据的模块图地址")
     public List<String> findGridByMonth(@RequestParam String year, @RequestParam String month) {
         String data = imgsservice.findNAOImgByMonth(year,month);
         int index = 0;
         List<String> naoList=new ArrayList<>();

         for(int i = 0; i<data.length();i++){
             char c = data.charAt(i);
             // 已逗号为分隔符分割图片地址
             if(c == ','){
                 naoList.add(data.substring(index,i));
                 index = i + 1;
             }
         }
         naoList.add(data.substring(index));

         return naoList;
     }

    /**
     * 初始化预报结果折线图
     * 因为观测数据是以年为单位存储的，而每次要返回六个月的数据，所以最晚可查询月份一定是12-5=7月
     * @param: null;
     * @return: Map<String, Object>.
     */
     @GetMapping("/initialize/naoPrediction")
     @ApiOperation(notes = "初始化预报结果折线图，返回可查询年月", value = "初始化预报结果折线图")
     public Map<String, Object> initialNAOPrediction(){
         List<Obs_nao> naoList = obs_naoservice.findNAOByModel("index_NAO_MCD");
         Map<String, Object> naoMap=new HashMap<>();

         // 返回最早可查询年月和最晚可查询年月
         String end_year=naoList.get(naoList.size()-1).getYear();
         naoMap.put("start_year",naoList.get(0).getYear());
         naoMap.put("start_month",naoList.get(0).getMonth());
         naoMap.put("end_year",naoList.get(naoList.size()-1).getYear());
         naoMap.put("end_month","7");

         // 按最晚可查询年月查询预报数据和观测数据
         Tj_nao preResult = tj_naoservice.findPredictionByMonthAndModel(end_year,"7");
         Obs_nao obsResult = obs_naoservice.findObservationByMonthAndModel(end_year,"1");
         String jsonString;
         ObjectMapper objectMapper=new ObjectMapper();
         double []pre_data = new double[6];
         double []obs_data = new double[6];
         double []data = null;
         try{
             jsonString = preResult.getData();
             pre_data = objectMapper.readValue(jsonString,double[].class);
             jsonString = obsResult.getData();
             data=objectMapper.readValue(jsonString,double[].class);
             for(int i = 0; i < 6; i++){
                 obs_data[i] = data[6 + i];
             }
         }catch (JsonProcessingException e){
             e.printStackTrace();
         }
         naoMap.put("predictionData", pre_data);
         naoMap.put("observationData", obs_data);

         return naoMap;
     }

    /**
     * 初始化预报结果模块图
     * @param: null;
     * @return: Map<String, Object>.
     */
    @GetMapping("/initialize/naoGrid")
    @ApiOperation(notes = "初始化预报结果折模块图，返回可查询年月", value = "初始化预报结果模块图")
     public Map<String, Object> initialNAOGrid(){
        List<Imgs> imgsList = imgsservice.findAllByType("NAO");
        Map<String, Object> naoMap=new HashMap<>();

        // 返回最早查询年月和最晚查询年月
        String end_year=imgsList.get(imgsList.size()-1).getYear();
        String end_month=imgsList.get(imgsList.size()-1).getMonth();
        naoMap.put("start_year",imgsList.get(0).getYear());
        naoMap.put("start_month",imgsList.get(0).getMonth());
        naoMap.put("end_year",end_year);
        naoMap.put("end_month",end_month);

        // 按最晚查询年月查询图片地址
        String data = imgsservice.findNAOImgByMonth(end_year,end_month);
        int index = 0;
        List<String> naoList=new ArrayList<>();
        for(int i = 0; i<data.length();i++){
            char c = data.charAt(i);
            // 以逗号为分隔符分割图片地址
            if(c == ','){
                naoList.add(data.substring(index,i));
                index = i + 1;
            }
        }
        naoList.add(data.substring(index));
        naoMap.put("data",naoList);

        return naoMap;
     }
}
