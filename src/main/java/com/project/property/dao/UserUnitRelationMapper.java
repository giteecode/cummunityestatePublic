package com.project.property.dao;

import com.project.property.entity.UserUnitRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;



@Mapper
public interface UserUnitRelationMapper {

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
    int insert(UserUnitRelation record);


    int insertSelective(UserUnitRelation record);

    UserUnitRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserUnitRelation record);

    int updateByPrimaryKey(UserUnitRelation record);

    List<UserUnitRelation> selectDataByParam(UserUnitRelation userUnitRelation);

    Integer selectDataCount(UserUnitRelation userUnitRelation);

    /**
     * 根据传入的ID查询信息
     * @param houseIds      房间ID
     * @param userIds       用户ID
     * @return
     */
    List<UserUnitRelation> selectInfoByIds(@Param("houseIds") String houseIds, @Param("userIds") String userIds);

    /**
     * 根据房间ID查询该房间是否已有户主
     * @param id
     * @return
     */
    UserUnitRelation selectInfoByHouseId(Integer id);

    /**
     * 根据多个主键查找
     * @param ids
     * @return
     */
    List<UserUnitRelation> selectByPrimaryKeys(@Param("ids") String ids);
}