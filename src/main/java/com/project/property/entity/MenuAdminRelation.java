package com.project.property.entity;

import lombok.Data;

/**
 * 菜单与管理员管理表
 */
@Data
public class MenuAdminRelation {
    private static final long serialVersionUID = 1L;
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 管理员ID
     */
    private Integer userId;

    /**
     * 菜单ID
     */
    private Integer menuId;

    /**
     * 菜单信息
     */
    private Menu menu;
}