package com.project.property.entity;

import lombok.Data;

/**
* 公告表
*/
@Data
public class Notice {
    /**
    * 自增主键
    */
    private Integer id;

    /**
    * 公告标题
    */
    private String title;

    /**
    * 公告内容
    */
    private String content;

    /**
    * 发布时间
    */
    private String createDate;

    /**
     * 公告图
     */
    private String newsImg;

    /**
     * 作者
     */
    private String userName;

    /**
     * 如果是用户发表的公告则绑定用户ID
     */
    private Integer userId;
}