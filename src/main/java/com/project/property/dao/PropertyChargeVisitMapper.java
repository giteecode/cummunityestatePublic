package com.project.property.dao;

import com.project.property.entity.PropertyChargeVisit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface PropertyChargeVisitMapper {

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
    int insert(PropertyChargeVisit record);

    /**
     * 选择性插入
     * @param record
     * @return
     */
    int insertSelective(PropertyChargeVisit record);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    PropertyChargeVisit selectByPrimaryKey(Integer id);

    /**
     * 根据主键选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(PropertyChargeVisit record);

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(PropertyChargeVisit record);

    /**
     * 根据条件查询数据
     * @param propertyChargeVisit
     * @return
     */
    List<PropertyChargeVisit> selectDataByParam(PropertyChargeVisit propertyChargeVisit);

    /**
     * 查询数据总数
     * @param propertyChargeVisit
     * @return
     */
    Integer selectDataCount(PropertyChargeVisit propertyChargeVisit);

    /**
     * 根据传入的ID查询信息，多个用逗号隔开
     * @param ids
     * @return
     */
    List<PropertyChargeVisit> selectInfoByIds(@Param("ids") String ids);

    /**
     * 根据收费项和房号查询该房间最近一次的缴费记录
     * @param itemId        收费项ID
     * @param houseNum      房间号
     * @return
     */
    PropertyChargeVisit selectByItemIdAndHouseNum(@Param("itemId") Integer itemId, @Param("houseNum") String houseNum);
}