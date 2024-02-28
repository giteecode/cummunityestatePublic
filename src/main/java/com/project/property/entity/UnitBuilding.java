package com.project.property.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @Commit 楼宇信息表
 */
@Data
public class UnitBuilding implements Serializable {
    /**
    * 主键ID
    */
    private Integer id;

    /**
    * 楼宇号
    */
    private Integer buildingNum;

    /**
    * 单元数量
    */
    private Integer unitCount;

    /**
    * 楼层
    */
    private Integer floorCount;

    private static final long serialVersionUID = 1L;
}