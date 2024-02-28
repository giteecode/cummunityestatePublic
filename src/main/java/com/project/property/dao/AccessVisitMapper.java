package com.project.property.dao;

import com.project.property.entity.AccessVisit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出入登记表数据库访问层
 */
@Mapper
public interface AccessVisitMapper {
    int deleteByPrimaryKey(@Param("ids") String ids);

    int insertSelective(AccessVisit record);

    AccessVisit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AccessVisit record);

    /**
     * 根据实体类作为参数查询
     * @param accessVisit
     * @return
     */
    List<AccessVisit> selectByParam(AccessVisit accessVisit);

    /**
     * 根据实体类作为参数查询数据总数
     * @param accessVisit
     * @return
     */
    Integer selectCountByParam(AccessVisit accessVisit);
}