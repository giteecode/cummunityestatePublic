package com.project.property.dao;

import com.project.property.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    int deleteByPrimaryKey(@Param("ids") String ids);

    int insertSelective(Comment comment);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment comment);

    /**
     * 根据参数查询
     * @param comment
     * @return
     */
    List<Comment> selectDataByParam(@Param("comment") Comment comment, @Param("type") String type);

    /**
     * 查询数据量
     * @param comment
     * @return
     */
    Integer selectCount(@Param("comment") Comment comment, @Param("type") String type);

    /**
     * 根据用户ID查询
     * @param ids
     * @return
     */
    List<Comment> selectInfoByUserId(@Param("ids") String ids);

    /**
     * 根据传入的ID将多条信息更新为删除状态
     * @param delIds
     * @return
     */
    Integer updateByDelete(@Param("ids") String delIds, @Param("type") String type);

    /**
     * 根据公告ID查询
     * @param noticeId
     * @return
     */
    List<Comment> selectInfoByNoticeId(Integer noticeId);
}