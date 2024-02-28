package com.project.property.dao;

import com.project.property.entity.UnitBuilding;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface UnitBuildingMapper {

    /**
     * 根据主键删除
     * @param ids
     * @return
     */
    int deleteByPrimaryKey(@Param("ids") String ids);

    /**
     * 插入方法
     * @param record
     * @return
     */
    int insert(UnitBuilding record);

    /**
     * 选择性插入
     * @param record
     * @return
     */
    int insertSelective(UnitBuilding record);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    UnitBuilding selectByPrimaryKey(Integer id);

    /**
     * 根据主键选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(UnitBuilding record);

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(UnitBuilding record);

    /**
     * 根据条件查询
     * @param unitBuilding  查询条件
     * @return
     */
    List<UnitBuilding> selectDataByParam(UnitBuilding unitBuilding);

    /**
     * 查询数据总数
     * @param unitBuilding
     * @return
     */
    Integer selectDataCount(UnitBuilding unitBuilding);

    /**
     * 根据传入的楼宇号查询楼宇信息
     * @param num
     * @return
     */
    UnitBuilding selectInfoByNum(Integer num);

    /**
     * 根据多个ID查询信息
     * @param ids
     * @return
     */
    List<UnitBuilding> selectInfoByIds(@Param("ids") String ids);
}