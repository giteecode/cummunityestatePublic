package com.project.property.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.property.dao.UserComplaintMapper;
import com.project.property.entity.UserComplaint;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class UserComplaintService {

    @Resource
    private UserComplaintMapper userComplaintMapper;

    /**
     * 根据主键删除
     *
     * @param ids
     * @return
     */
    public int deleteByPrimaryKey(String ids) {
        // 判断是否可以删除（未处理不可删除）
        List<UserComplaint> complaintList = userComplaintMapper.selectByIdAndStatus(ids);
        if (complaintList != null && complaintList.size() > 0) {
            return -500;
        }
        return userComplaintMapper.deleteByPrimaryKey(ids);
    }

    /**
     * 选择性插入
     *
     * @param record
     * @return
     */
    public int insertSelective(UserComplaint record) {
        // 补充数据
        record.setCreateDate(DateUtil.now());
        record.setIsSolve("0");
        return userComplaintMapper.insertSelective(record);
    }

    /**
     * 根据主键查询单条数据
     *
     * @param id
     * @return
     */
    public UserComplaint selectByPrimaryKey(Integer id) {
        return userComplaintMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据主键选择性更新
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(UserComplaint record) {
        return userComplaintMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 分页查询数据
     *
     * @param userComplaint
     * @param page
     * @param limit
     * @return
     */
    public List<UserComplaint> selectDataByPage(UserComplaint userComplaint, Integer page, Integer limit) {
        // 开启分页插件
        PageHelper.startPage(page, limit);
        // 使用分页插件处理数据
        PageInfo<UserComplaint> pageInfo = new PageInfo<UserComplaint>(userComplaintMapper.selectDataByParam(userComplaint));
        // 返回数据
        return pageInfo.getList();
    }

    /**
     * 查询数据总量
     *
     * @param userComplaint
     * @return
     */
    public Integer selectDataCount(UserComplaint userComplaint) {
        return userComplaintMapper.selectDataCount(userComplaint);
    }


    /**
     * 统计分析
     *
     * @return
     */
    public Map statisticAnalysis() {
        // 获取要分析的数据
        List<UserComplaint> list = userComplaintMapper.selectDataByParam(new UserComplaint());

        // 满意度统计
        Map<String, Long> count2 = list.stream()
                .filter(o -> StrUtil.isNotBlank(o.getDegree()))   // 过滤评价为空的数据
                .collect(Collectors.groupingBy(UserComplaint::getDegree, Collectors.counting())); // 按评价分组，汇总数量

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
