package com.project.property.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.project.property.entity.MenuAdminRelation;
import com.project.property.dao.MenuAdminRelationMapper;

import java.util.List;


@Service
public class MenuAdminRelationService{

    @Resource
    private MenuAdminRelationMapper menuAdminRelationMapper;

    
    public int insertSelective(MenuAdminRelation record) {
        return menuAdminRelationMapper.insertSelective(record);
    }

    /**
     * 根据用户ID查询该用户的菜单权限
     * @param userId
     * @return
     */
    public List<MenuAdminRelation> selectInfoByUserId(String userId) {
        return menuAdminRelationMapper.selectInfoByUserId(userId);
    }

}
