package com.project.property.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @Commit 用户报修信息表
 */
@Data
public class UserRepair implements Serializable {
    /**
    * 自增主键
    */
    private Integer id;

    /**
    * 客户姓名
    */
    private String userName;

    /**
    * 客户电话
    */
    private String phone;

    /**
    * 报修信息
    */
    private String repairInfo;

    /**
    * 创建时间
    */
    private String createDate;

    /**
     * 是否处理  0未处理  1已处理
     */
    private String isSolve;

    /**
     * 将isSolve判断后记录为文字展示
     */
    private String isSolveStr;

    /**
     * 对应的用户ID
     */
    private Integer userId;

    /**
     * 分配的员工ID
     */
    private Integer empId;

    /**
     * 处理结果
     */
    private String resultMsg;

    /**
     * 用户反馈
     */
    private String feedbackMsg;

    /**
     * 员工实体
     */
    private Employee employee;

    /**
     * 情感分
     */
    private String score;


    /**
     * 评价等级
     */
    private String degree;

    private static final long serialVersionUID = 1L;
}