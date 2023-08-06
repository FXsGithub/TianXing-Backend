package com.tongji.enso.mybatisdemo.controller;

import com.tongji.enso.mybatisdemo.entity.online.Imgs;
import com.tongji.enso.mybatisdemo.mapper.online.ImgsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/imgs")
public class ImgsController {

    @Autowired
    private ImgsMapper imgsMapper;

    /**
     * 获取指定 年、月、日 的图片路径 WEA_MSLP
     * eg. http://localhost:8080/imgs/WEA_MSLP/getImgsPath?year=2019&month=1&day=1
     * @param year
     * @param month
     * @param day
     * @return
     */
    @RequestMapping("/WEA_MSLP/getImgsPath")
    public Map<String, Object> getMSLPImgsPathByDay(String year, String month, String day) {
        List<Imgs> imgsData = imgsMapper.findImgsInfoByDayType(year, month, day, "WEA_MSLP");

        // 将 data 字段从 JSON 字符串转换为 List<String>
        List<String> imgPaths = new ArrayList<>();
        if (!imgsData.isEmpty()) {
            String imgSrcData = imgsData.get(0).getData();
            imgPaths = Arrays.asList(imgSrcData.split(","));
        }

        Map<String, Object> result = new HashMap<>();

        // 从 0 时开始，每次 + 6
        int hour = 0;
        List<String> titlesList = new ArrayList<>();
        for (int i = 0; i < imgPaths.size(); i++)
        {
            titlesList.add(String.format("%s年%s月%s日%d时待定待定", year, month, day, hour));
            hour+= 6;
        }

        List<String> testsList = new ArrayList<>();
        for (int i = 0; i < imgPaths.size(); i++)
        {
            testsList.add("待定待定");
        }

        result.put("titles", titlesList);
        result.put("imgSrc", imgPaths);
        result.put("texts", testsList);

        return result;
    }

    /**
     * 获取指定 年、月、日 的图片路径 WEA_T2M
     * eg. http://localhost:8080/imgs/WEA_T2M/getImgsPath?year=2019&month=1&day=1
     * @param year
     * @param month
     * @param day
     * @return
     */
    @RequestMapping("/WEA_T2M/getImgsPath")
    public Map<String, Object> getT2MImgsPathByDay(String year, String month, String day) {
        List<Imgs> imgsData = imgsMapper.findImgsInfoByDayType(year, month, day, "WEA_T2M");

        // 将 data 字段从 JSON 字符串转换为 List<String>
        List<String> imgPaths = new ArrayList<>();
        if (!imgsData.isEmpty()) {
            String imgSrcData = imgsData.get(0).getData();
            imgPaths = Arrays.asList(imgSrcData.split(","));
        }

        Map<String, Object> result = new HashMap<>();

        // 从 0 时开始，每次 + 6
        int hour = 0;
        List<String> titlesList = new ArrayList<>();
        for (int i = 0; i < imgPaths.size(); i++)
        {
            titlesList.add(String.format("%s年%s月%s日%d时2米气温预测结果", year, month, day, hour));
            hour+= 6;
        }

        List<String> testsList = new ArrayList<>();
        for (int i = 0; i < imgPaths.size(); i++)
        {
            testsList.add("全球整体气温偏高，澳大利亚出现异常高温");
        }

        result.put("titles", titlesList);
        result.put("imgSrc", imgPaths);
        result.put("texts", testsList);

        return result;
    }

    /**
     * 获取指定 年、月、日 的图片路径 WEA_TP
     * eg. http://localhost:8080/imgs/WEA_TP/getImgsPath?year=2019&month=1&day=1
     */
    @RequestMapping("/WEA_TP/getImgsPath")
    public Map<String, Object> getTPImgsPathByDay(String year, String month, String day) {
        List<Imgs> imgsData = imgsMapper.findImgsInfoByDayType(year, month, day, "WEA_TP");

        // 将 data 字段从 JSON 字符串转换为 List<String>
        List<String> imgPaths = new ArrayList<>();
        if (!imgsData.isEmpty()) {
            String imgSrcData = imgsData.get(0).getData();
            imgPaths = Arrays.asList(imgSrcData.split(","));
        }

        Map<String, Object> result = new HashMap<>();

        // 从 0 时开始，每次 + 6
        int hour = 0;
        List<String> titlesList = new ArrayList<>();
        for (int i = 0; i < imgPaths.size(); i++)
        {
            titlesList.add(String.format("%s年%s月%s日%d时地表降水预测结果", year, month, day, hour));
            hour+= 6;
        }

        List<String> testsList = new ArrayList<>();
        for (int i = 0; i < imgPaths.size(); i++)
        {
            testsList.add("赤道地区与北半球降水较多，南半球降水较少");
        }

        result.put("titles", titlesList);
        result.put("imgSrc", imgPaths);
        result.put("texts", testsList);

        return result;
    }

    /**
     * 获取指定 年、月、日 的图片路径 WEA_U10
     * eg. http://localhost:8080/imgs/WEA_U10/getImgsPath?year=2019&month=1&day=1
     */

    @RequestMapping("/WEA_U10/getImgsPath")
    public Map<String, Object> getU10ImgsPathByDay(String year, String month, String day) {
        List<Imgs> imgsData = imgsMapper.findImgsInfoByDayType(year, month, day, "WEA_U10");

        // 将 data 字段从 JSON 字符串转换为 List<String>
        List<String> imgPaths = new ArrayList<>();
        if (!imgsData.isEmpty()) {
            String imgSrcData = imgsData.get(0).getData();
            imgPaths = Arrays.asList(imgSrcData.split(","));
        }

        Map<String, Object> result = new HashMap<>();

        // 从 0 时开始，每次 + 6
        int hour = 0;
        List<String> titlesList = new ArrayList<>();
        for (int i = 0; i < imgPaths.size(); i++)
        {
            titlesList.add(String.format("%s年%s月%s日%d时待定待定", year, month, day, hour));
            hour+= 6;
        }

        List<String> testsList = new ArrayList<>();
        for (int i = 0; i < imgPaths.size(); i++)
        {
            testsList.add("待定待定");
        }

        result.put("titles", titlesList);
        result.put("imgSrc", imgPaths);
        result.put("texts", testsList);

        return result;
    }

    /**
     * 初始化：返回可选年、月、日范围
     * eg. http://localhost:8080/imgs/WEA_MSLP/getInitData
     */
    @RequestMapping("/WEA_MSLP/getInitData")
    public Map<String, Object> getMSLPInitMonth()
    {
        List<Imgs> imgsData = imgsMapper.findImgsInfoByType("WEA_MSLP");

        List<String> years = new ArrayList<>();
        List<String> months = new ArrayList<>();
        List<String> days = new ArrayList<>();

        for (Imgs img : imgsData)
        {
            years.add(img.getYear());
            months.add(img.getMonth());
            days.add(img.getDay());
        }

        Map<String, Object> result = new HashMap<>();

        result.put("years", years);
        result.put("months", months);
        result.put("days", days);

        return result;
    }

}
