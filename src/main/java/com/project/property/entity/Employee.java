package com.project.property.entity;

import lombok.Data;

/**
 * @description 文件描述
 */
/**
    * 员工表
    */
@Data
public class Employee {
    /**
    * 自增主键
    */
    private Integer id;

    /**
    * 员工名
    */
    private String empName;

    /**
    * 员工电话
    */
    private String empPhone;

    /**
    * 员工性别
    */
    private String empSex;

    /**
    * 员工身份证号
    */
    private String empIdCard;

    /**
    * 员工年龄
    */
    private Integer empAge;

    /**
    * 员工住址
    */
    private String empAddress;
}