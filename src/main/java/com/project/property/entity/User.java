package com.project.property.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Commit 住户信息表
 */
@Data
public class User implements Serializable {
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 电话
     */
    private String phone;

    /**
     * 身份证号
     */
    private String cardNum;

    /**
     * 性别
     */
    private String sex;

    /**
     * 民族
     */
    private String nation;

    /**
     * 户籍地址
     */
    private String registerAddress;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 登录密码
     */
    private String password;

    private static final long serialVersionUID = 1L;
}