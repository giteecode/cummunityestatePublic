package com.project.property.entity;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;


@Data
public class TpUser {
    /**
    * 自动编号
    */
    private Integer id;

    private String username;

    private String password;

    private String gender;

    private String email;

    private BigDecimal price;

    private String details;

    private Short uid;

    /**
    * 状态
    */
    private Byte status;

    private String list;

    private Date deleteTime;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 修改时间
    */
    private Date updateTime;
}