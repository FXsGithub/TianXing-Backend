package com.tongji.enso.mybatisdemo.controller;

import com.tongji.enso.mybatisdemo.entity.online.Tj_sie;
import com.tongji.enso.mybatisdemo.service.online.Tj_sieService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seaice")
public class Tj_sieController {
    @Autowired
    private Tj_sieService tj_sieService;

    /**
     * 查询某月份开始起报之后12个月的SIE指数
     */
    @GetMapping("/predictionResult/SIE")
    @ApiOperation(value = "查询月份开始起报之后12个月的SIE指数以及文本描述", notes = "根据月份查询SIE指数预测结果")
    public List<Tj_sie> findByMonth(@RequestParam String year, @RequestParam String month){
        return tj_sieService.findSIEByMonth(year,month);
    }
}
