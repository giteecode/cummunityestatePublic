package com.project.property.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @Commit 用户与住房关系对应表
 */
@Data
public class UserUnitRelation implements Serializable {
    /**
    * 自增主键
    */
    private Integer id;

    /**
    * 用户ID
    */
    private Integer userId;

    /**
    * 住房ID
    */
    private Integer unitId;

    /**
    * 创建时间
    */
    private String createDate;

    /**
    * 创建时间
    */
    private String isHouseHolder;

    /**
     * 住户信息
     */
    private User user;

    /**
     * 房间信息
     */
    private HouseInfo houseInfo;

    private static final long serialVersionUID = 1L;
}