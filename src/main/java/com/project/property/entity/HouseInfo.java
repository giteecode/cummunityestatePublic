package com.project.property.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @Commit 住房详细信息
 */
@Data
public class HouseInfo implements Serializable {
    /**
    * 自增主键
    */
    private Integer id;

    /**
    * 所属楼宇
    */
    private String parentBuilding;

    /**
    * 所属单元
    */
    private String parentUnit;

    /**
    * 所属楼层
    */
    private String parentFloor;

    /**
    * 房间号
    */
    private String houseNum;

    /**
    * 房间大小
    */
    private String houseArea;

    /**
    * 房间户型
    */
    private String houseType;

    /**
    * 户主
    */
    private String ownerName;

    /**
    * 联系电话
    */
    private String phone;

    /**
    * 是否售出：0未售出1已售出2待出租3已出租
    */
    private String isSold;

    /**
     * 绑定的用户ID
     */
    private Integer userId;

    private static final long serialVersionUID = 1L;
}