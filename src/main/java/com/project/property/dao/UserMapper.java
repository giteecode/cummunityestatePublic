package com.project.property.dao;

import com.project.property.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

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
    int insert(User record);

    /**
     * 选择性插入
     * @param record
     * @return
     */
    int insertSelective(User record);

    /**
     * 根据主键查询单条信息
     * @param id
     * @return
     */
    User selectByPrimaryKey(Integer id);

    /**
     * 根据主键选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(User record);

    /**
     * 根据条件查询
     * @param user
     * @return
     */
    List<User> selectDataByParam(User user);

    /**
     * 查询数据总量
     * @param user
     * @return
     */
    Integer selectDataCount(User user);

    /**
     * 前台登陆
     * @param user
     * @return
     */
    User selectLoginByParam(User user);

    /**
     * 根据用户名+手机号获取
     * @param user
     * @return
     */
    User selectUserByParam(User user);
}