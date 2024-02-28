package com.project.property.dao;

import com.project.property.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 */
@Mapper
public interface AdminMapper {

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入
     * @param record
     * @return
     */
    int insert(Admin record);

    /**
     * 选择性插入(NULL不插入)
     * @param record
     * @return
     */
    int insertSelective(Admin record);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    Admin selectByPrimaryKey(Integer id);

    /**
     * 根据主键选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Admin record);

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(Admin record);

    /**
     * 根据Admin条件查询
     * @param admin 查询条件
     * @return 返回admin集合
     */
    List<Admin> selectByAdmin(Admin admin);

    /**
     * 根据条件查询
     * @param admin
     * @return
     */
    Integer selectDataCount(Admin admin);

    /**
     * 查询数据总数
     * @param admin
     * @return
     */
    List<Admin> selectDataByParam(Admin admin);
}