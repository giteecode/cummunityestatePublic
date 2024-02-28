package com.project.property.dao;

import com.project.property.entity.HouseInfo;
import com.project.property.entity.UnitBuilding;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface HouseInfoMapper {

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(@Param("ids") String id);

    /**
     * 全量插入
     * @param record
     * @return
     */
    int insert(HouseInfo record);

    /**
     * 选择性插入
     * @param record
     * @return
     */
    int insertSelective(HouseInfo record);

    /**
     * 根据主键查询数据
     * @param id
     * @return
     */
    HouseInfo selectByPrimaryKey(Integer id);

    /**
     * 根据主键选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(HouseInfo record);

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(HouseInfo record);

    /**
     * 更新房屋出租情况
     * @param record
     * @return
     */
    int updateSoldInfoByPrimaryKey(HouseInfo record);

    /**
     * 根据条件查询数据
     * @param houseInfo
     * @return
     */
    List<HouseInfo> selectDataByParam(HouseInfo houseInfo);

    /**
     * 查询数据总量
     * @param houseInfo
     * @return
     */
    Integer selectDataCount(HouseInfo houseInfo);

    /**
     * 根据多个id查询信息
     * @param ids
     * @return
     */
    List<HouseInfo> selectInfoByIds(@Param("ids") String ids);

    /**
     * 根据多个楼号
     * @param nums
     * @return
     */
    List<HouseInfo> selectInfoByBuildingNum(@Param("nums") String nums);

    /**
     * 根据楼号  单元号  楼层    房间号四个条件判断
     * @param parentBuilding    楼号
     * @param parentUnit        单元号
     * @param parentFloor       楼层
     * @param houseNum          房号
     * @return
     */
    HouseInfo selectByHouseInfo(@Param("building") String parentBuilding, @Param("unit") String parentUnit,
                                @Param("floor") String parentFloor, @Param("houseNum") String houseNum);

    Integer updateInfoByUserId(HouseInfo houseInfo);
}