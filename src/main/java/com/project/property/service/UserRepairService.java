package com.project.property.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.property.dao.UserRepairMapper;
import com.project.property.entity.UserRepair;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class UserRepairService {

    /**
     * 数据访问层对象
     */
    @Resource
    private UserRepairMapper userRepairMapper;

    /**
     * 根据主键删除
     *
     * @param ids
     * @return
     */
    public int deleteByPrimaryKey(String ids) {
        // 判断是否可以删除（未处理不可删除）
        List<UserRepair> repairs = userRepairMapper.selectByIdAndStatus(ids);
        if (repairs != null && repairs.size() > 1) {
            return -500;
        }
        return userRepairMapper.deleteByPrimaryKey(ids);
    }

    /**
     * 选择性插入
     *
     * @param record
     * @return
     */
    public int insertSelective(UserRepair record) {
        // 补充数据
        record.setCreateDate(DateUtil.now());
        record.setIsSolve("0");
        return userRepairMapper.insertSelective(record);
    }

    /**
     * 根据主键查询单条数据
     *
     * @param id
     * @return
     */
    public UserRepair selectByPrimaryKey(Integer id) {
        return userRepairMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据主键选择性更新
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(UserRepair record) {
        return userRepairMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 分页查询全部数据
     *
     * @param userRepair 查询条件
     * @param page       当前页
     * @param limit      每页显示的数据量
     * @return
     */
    public List<UserRepair> selectDataByPage(UserRepair userRepair, Integer page, Integer limit) {
        // 开启分页插件
        PageHelper.startPage(page, limit);
        // 使用分页插件查询
        PageInfo<UserRepair> pageInfo = new PageInfo<UserRepair>(userRepairMapper.selectDataByParam(userRepair));
        // 返回数据
        return pageInfo.getList();
    }

    /**
     * 查询数据总量
     *
     * @param userRepair
     * @return
     */
    public Integer selectDataCount(UserRepair userRepair) {
        return userRepairMapper.selectDataCount(userRepair);
    }


    /**
     * 统计分析
     *
     * @return
     */
    public Map statisticAnalysis() {
        // 获取要分析的数据
        List<UserRepair> list = userRepairMapper.selectDataByParam(new UserRepair());

        // 满意度统计
        Map<String, Long> count2 = list.stream()
                .filter(o -> StrUtil.isNotBlank(o.getDegree()))   // 过滤评价为空的数据
                .collect(Collectors.groupingBy(UserRepair::getDegree, Collectors.counting())); // 按评价分组，汇总数量

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
