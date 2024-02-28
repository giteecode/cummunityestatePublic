package com.project.property.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.property.dao.CommentMapper;
import com.project.property.entity.Comment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评论
 */
@Service
public class CommentService {

    @Resource
    private CommentMapper commentMapper;


    public int deleteByPrimaryKey(String ids) {
        return commentMapper.deleteByPrimaryKey(ids);
    }


    public int insertSelective(Comment record) {
        return commentMapper.insertSelective(record);
    }


    public Comment selectByPrimaryKey(Integer id) {
        return commentMapper.selectByPrimaryKey(id);
    }


    public int updateByPrimaryKeySelective(Comment record) {
        return commentMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 分页查询数据
     *
     * @param comment
     * @param page
     * @param limit
     * @return
     */
    public List<Comment> selectDataByPage(Comment comment, Integer page, Integer limit, String type) {
        // 开启分页
        PageHelper.startPage(page, limit);
        PageInfo<Comment> pageInfo = new PageInfo<Comment>(commentMapper.selectDataByParam(comment, type));
        return pageInfo.getList();
    }

    /**
     * 查询数据总条数
     *
     * @param comment
     * @return
     */
    public Integer selectDataCount(Comment comment, String type) {
        return commentMapper.selectCount(comment, type);
    }

    /**
     * 根据用户ID查询
     *
     * @param ids
     * @return
     */
    public List<Comment> selectInfoByUserId(String ids) {
        return commentMapper.selectInfoByUserId(ids);
    }

    /**
     * 逻辑删除
     *
     * @param delIds
     * @return
     */
    public Integer updateByDelete(String delIds, String type) {
        return commentMapper.updateByDelete(delIds, type);
    }

    /**
     * 根据公告ID查询
     *
     * @param noticeId
     * @return
     */
    public List<Comment> selectInfoByNoticeId(Integer noticeId) {
        return commentMapper.selectInfoByNoticeId(noticeId);
    }

    /**
     * 统计分析
     *
     * @return
     */
    public Map statisticAnalysis() {
        // 获取要分析的数据
        List<Comment> list = commentMapper.selectDataByParam(new Comment(), null);

        // 满意度统计
        Map<String, Long> count2 = list.stream()
                .filter(o -> StrUtil.isNotBlank(o.getDegree()))   // 过滤评价为空的数据
                .collect(Collectors.groupingBy(Comment::getDegree, Collectors.counting())); // 按评价分组，汇总数量

        List<Map> appraiseCount = new ArrayList<>();
        count2.entrySet().forEach(o -> {
            Map<String, Object> m = new HashMap<>();
            m.put("name", o.getKey());
            m.put("value", o.getValue());
            appraiseCount.add(m);
        });

        // 统计结果封装
        Map<String, Object> result = new HashMap<>();
        result.put("chart1", appraiseCount);

        return result;
    }
}
