package com.project.property.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @Commit 物业收费项目信息表
 */
@Data
public class PropertyChargeItem implements Serializable {
    /**
    * 自增主键
    */
    private Integer id;

    /**
    * 收费项目名
    */
    private String chargeName;

    /**
    * 项目单价
    */
    private Integer itemPrice;

    /**
    * 价格描述
    */
    private String priceDesc;


    /**
    * 创建时间
    */
    private String createDate;

    private static final long serialVersionUID = 1L;
}