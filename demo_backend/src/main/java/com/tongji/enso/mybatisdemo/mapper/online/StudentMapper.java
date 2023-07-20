package com.tongji.enso.mybatisdemo.mapper.online;


import com.tongji.enso.mybatisdemo.entity.online.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentMapper {

    /**
     * 查询全部用户
     */
    List<Student> findAll();

    /**
     * 根据ID查询学生
     * @param: id;
     * @return: Student.
     */
    Student findById(int id);
}
