package com.project.property.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Commit 停车场表
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarParkCharge implements Serializable {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 车位信息D
     */
    private Integer carParkId;

    /**
     * 业主名
     */
    private String ownerName;

    /**
     * 业主联系电话
     */
    private String phone;

    /**
     * 费用
     */
    private BigDecimal fee;

    /**
     * 收费日期
     */
    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm:ss", timezone = "GMT+8")
    private Date chargeDate;


    /**
     * 收费人
     */
    private String readName;

    /**
     * 缴费方式
     */
    private String payType;

    /**
     * 停车场信息
     */
    private CarPark carParkInfo;

    private static final long serialVersionUID = 1L;
}