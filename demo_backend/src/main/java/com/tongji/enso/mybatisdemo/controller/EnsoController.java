package com.tongji.enso.mybatisdemo.controller;

import com.tongji.enso.mybatisdemo.entity.online.Enso;
import com.tongji.enso.mybatisdemo.mapper.online.EnsoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/enso")
public class EnsoController {

    @Autowired
    private EnsoMapper ensoMapper;

    /**
     * 调用 EnsoMapper 接口的方法，传入请求中的 year 和 month 参数，以获取接下来18个月的 Enso 数据
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/predictionResult/linechart")
    public List<Enso> findPredictionsForNext18Months(@RequestParam("year") String year, @RequestParam("month") String month) {
        return ensoMapper.findPredictionsForNext18Months(year, month);
    }

}
