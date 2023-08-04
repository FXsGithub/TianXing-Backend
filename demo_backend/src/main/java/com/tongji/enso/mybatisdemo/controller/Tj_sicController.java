package com.tongji.enso.mybatisdemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongji.enso.mybatisdemo.entity.online.Info_sic_latlon;
import com.tongji.enso.mybatisdemo.entity.online.Tj_sic;
import com.tongji.enso.mybatisdemo.service.online.ImgsService;
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
    private ImgsService imgsservice;

    /**
     * 查询全部的SIC指数
     */
    @GetMapping("/findAllSIC")
    @ApiOperation(notes = "查询全部Sic指数，返回'Tj_sic'类型列表", value = "查询全部Sic指数")
    public List<Tj_sic> findAll(){ return tj_sicservice.findAllSIC(); }

    /**
     * 查询某年某月某日的SIC指数的模块图
     * @param: year, month, day;
     * @return: List<String>.
     */
    @GetMapping("/predictionResult/SIC")
    @ApiOperation(notes = "查询指定日期SIC指数的模块图", value = "根据日期查询SIC指数模块图的地址")
    public List<String> findSICPredictionByDate(@RequestParam String year,@RequestParam String month,@RequestParam String day){

        String data = imgsservice.findSICImgByDate(year,month,day);
        int index = 0;
        List<String> sicList=new ArrayList<>();

        for(int i = 0; i<data.length();i++){
            char c = data.charAt(i);
            if(c == ','){
                sicList.add(data.substring(index,i));
                index = i + 1;
            }
        }
        sicList.add(data.substring(index));

        return sicList;
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
