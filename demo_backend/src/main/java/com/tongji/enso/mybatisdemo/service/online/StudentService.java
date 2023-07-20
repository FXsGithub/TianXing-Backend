package com.tongji.enso.mybatisdemo.service.online;

import com.tongji.enso.mybatisdemo.entity.online.Student;
import com.tongji.enso.mybatisdemo.mapper.online.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

//    命名要有具体含义
    public List<Student> findAllStudent(){
        return studentMapper.findAll();
    }

    public Student findById(int id){
        return studentMapper.findById(id);
    }

}
