package com.tongji.enso.mybatisdemo.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.tongji.enso.mybatisdemo.entity.online.Student;
import com.tongji.enso.mybatisdemo.service.online.StudentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {


    /**
     * 这里简单展示了一个例子，具体开发中会涉及到更复杂的数据结构，也会涉及到要将返回数据处理成前端echarts要求的数据格式；
     * 此处只是说明代码规范；
     */

    @Autowired
    private StudentService studentService;

    /**
     * 查询全部学生信息
     */
    @GetMapping("/findAll")
    @ApiOperation(value = "查询全部学生，返回'学生'类型列表", notes = "查询全部学生")
    public List<Student> findAll(){
        return studentService.findAllStudent();
    }

    /**
     * 根据ID查询学生
     * @param: id;
     * @return: Student.
     */
    @GetMapping("/findById/{id}")
    @ApiOperation(value = "根据ID查询学生，返回查询的'学生'", notes = "根据ID查询学生")
    public Student findById(@PathVariable int id){
        return studentService.findById(id);
    }

    /**
     * 根据ID查询学生
     * @param: id;
     * @return: Student.
     */
    @GetMapping("/finddata/{id}")
    @ApiOperation(value = "数据格式处理demo", notes = "JSON字符串转成多维数组的demo")
    public HashMap<String, Object> findDataById(@PathVariable int id){

        Student stu = studentService.findById(id);
        String jsonString = stu.getData();
        double[][][] data_trans = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            data_trans = objectMapper.readValue(jsonString, double[][][].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HashMap<String, Object> return_hashmap = new HashMap<String, Object>();
        return_hashmap.put("data", data_trans);

        return return_hashmap;
    }

}
