package com.project.property.dao;

import com.project.property.entity.PropertyChargeItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface PropertyChargeItemMapper {

    /**
     * 根据主键删除
     * @param ids
     * @return
     */
    int deleteByPrimaryKey(@Param("ids") String ids);

    /**
     * 全量插入
     * @param record
     * @return
     */
    int insert(PropertyChargeItem record);

    /**
     * 选择性插入
     * @param record
     * @return
     */
    int insertSelective(PropertyChargeItem record);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    PropertyChargeItem selectByPrimaryKey(Integer id);

    /**
     * 根据主键选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(PropertyChargeItem record);

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(PropertyChargeItem record);

    /**
     * 根据条件查询
     * @param propertyChargeItem
     * @return
     */
    List<PropertyChargeItem> selectDataByParam(PropertyChargeItem propertyChargeItem);

    /**
     * 查询数据总量
     * @param propertyChargeItem
     * @return
     */
    Integer selectDataCount(PropertyChargeItem propertyChargeItem);

    /**
     * 根据传入的ID查询信息
     * @param ids
     * @return
     */
    List<PropertyChargeItem> selectInfoByIds(@Param("ids") String ids);
}