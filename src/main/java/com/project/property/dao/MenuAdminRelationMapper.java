package com.project.property.dao;

import com.project.property.entity.MenuAdminRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuAdminRelationMapper {

    int insertSelective(MenuAdminRelation record);

    /**
     * 根据用户ID查询用户权限
     *
     * @param userId
     * @return
     */
    List<MenuAdminRelation> selectInfoByUserId(String userId);
}