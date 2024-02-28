package com.project.property.dao;

import com.project.property.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {
    int deleteByPrimaryKey(@Param("ids") String ids);

    int insertSelective(Notice record);

    Notice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Notice record);

    /**
     * 根据参数查询
     * @param notice
     * @return
     */
    List<Notice> selectDataByParam(Notice notice);

    /**
     * 查询数据量
     * @param notice
     * @return
     */
    Integer selectCount(Notice notice);
}