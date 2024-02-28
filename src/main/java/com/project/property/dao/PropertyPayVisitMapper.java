package com.project.property.dao;

import com.project.property.entity.PropertyPayVisit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface PropertyPayVisitMapper {

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 全量插入
     * @param record
     * @return
     */
    int insert(PropertyPayVisit record);

    /**
     * 选择性插入数据
     * @param record
     * @return
     */
    int insertSelective(PropertyPayVisit record);

    /**
     * 根据主键查询单条数据
     * @param id
     * @return
     */
    PropertyPayVisit selectByPrimaryKey(Integer id);

    /**
     * 根据主键选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(PropertyPayVisit record);

    /**
     * 根据主键更新数据信息
     * @param record
     * @return
     */
    int updateByPrimaryKey(PropertyPayVisit record);

    /**
     * 根据条件查询数据
     * @param propertyPayVisit
     * @return
     */
    List<PropertyPayVisit> selectDataByParam(PropertyPayVisit propertyPayVisit);

    /**
     * 查询数据总量
     * @param propertyPayVisit
     * @return
     */
    Integer selectDataCount(PropertyPayVisit propertyPayVisit);

    List<PropertyPayVisit> selectInfoByUserName(@Param("userName") String userName);
}