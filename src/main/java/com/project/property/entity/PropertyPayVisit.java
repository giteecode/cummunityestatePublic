package com.project.property.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 物业缴费记录信息表
 */
@Data
public class PropertyPayVisit implements Serializable {
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 对应物业收费记录表的ID
     */
    private String chargeId;

    /**
     * 楼宇号
     */
    private String buildingNum;

    /**
     * 房间号
     */
    private String houseNum;

    /**
     * 客户姓名
     */
    private String clientName;

    /**
     * 收费项目
     */
    private String itemName;

    /**
     * 缴费金额（单位：分）
     */
    private Integer payMoney;

    /**
     * 缴费方式
     */
    private String payType;

    /**
     * 收费人员
     */
    private String chargePerson;

    /**
     * 缴费时间
     */
    private String payDate;

    /**
     * 单元号
     */
    private String unitNum;

    private static final long serialVersionUID = 1L;
}