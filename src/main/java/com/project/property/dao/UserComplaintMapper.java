package com.project.property.dao;

import com.project.property.entity.UserComplaint;
import com.project.property.entity.UserRepair;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface UserComplaintMapper {

    /**
     * 根据主键删除
     * @param ids
     * @return
     */
    int deleteByPrimaryKey(@Param("ids") String ids);

    /**
     * 选择性插入
     * @param record
     * @return
     */
    int insertSelective(UserComplaint record);

    /**
     * 根据主键查询单条数据
     * @param id
     * @return
     */
    UserComplaint selectByPrimaryKey(Integer id);

    /**
     * 根据主键选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(UserComplaint record);

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(UserComplaint record);

    /**
     * 查询数据总量
     * @param userComplaint
     * @return
     */
    Integer selectDataCount(UserComplaint userComplaint);

    /**
     * 根据条件查询
     * @param userComplaint
     * @return
     */
    List<UserComplaint> selectDataByParam(UserComplaint userComplaint);

    /**
     * 查询ID和状态是否是未处理
     * @param ids
     * @return
     */
    List<UserComplaint> selectByIdAndStatus(@Param("ids") String ids);

    Integer updateInfoByUserId(UserComplaint userComplaint);
}