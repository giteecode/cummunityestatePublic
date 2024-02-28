package com.project.property.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.project.property.entity.Menu;
import com.project.property.dao.MenuMapper;

@Service
public class MenuService{

    @Resource
    private MenuMapper menuMapper;

    
    public int deleteByPrimaryKey(Integer id) {
        return menuMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(Menu record) {
        return menuMapper.insert(record);
    }

    
    public int insertSelective(Menu record) {
        return menuMapper.insertSelective(record);
    }

    
    public Menu selectByPrimaryKey(Integer id) {
        return menuMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(Menu record) {
        return menuMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(Menu record) {
        return menuMapper.updateByPrimaryKey(record);
    }

}
