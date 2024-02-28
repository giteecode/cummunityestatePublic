package com.project.property.service;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.property.entity.User;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.project.property.entity.Employee;
import com.project.property.dao.EmployeeMapper;

import java.util.List;

/**
 * @description 文件描述
 */
@Service
public class EmployeeService{

    @Resource
    private EmployeeMapper employeeMapper;

    /**
     * 根据主键删除
     * @param ids
     * @return
     */
    public int deleteByPrimaryKey(String ids) {
        return employeeMapper.deleteByPrimaryKey(ids);
    }

    /**
     * 选择性新增
     * @param record
     * @return
     */
    public int insertSelective(Employee record) {
        // 判断如果身份证号不为空则查询身份证号信息
        if(!StrUtil.isBlank(record.getEmpIdCard())) {
            // 使用Hutool工具类解析身份证
            // 户籍信息
            record.setEmpAge(IdcardUtil.getAgeByIdCard(record.getEmpIdCard()));
            // 性别
            record.setEmpSex(IdcardUtil.getGenderByIdCard(record.getEmpIdCard()) == 0 ? "女" : "男");
        }
        return employeeMapper.insertSelective(record);
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    public Employee selectByPrimaryKey(Integer id) {
        return employeeMapper.selectByPrimaryKey(id);
    }

    /**
     * 选择性更新
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(Employee record) {
        // 判断如果身份证号不为空则查询身份证号信息
        if(!StrUtil.isBlank(record.getEmpIdCard())) {
            // 使用Hutool工具类解析身份证
            // 户籍信息
            record.setEmpAge(IdcardUtil.getAgeByIdCard(record.getEmpIdCard()));
            // 性别
            record.setEmpSex(IdcardUtil.getGenderByIdCard(record.getEmpIdCard()) == 0 ? "女" : "男");
        }
        return employeeMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 分页查询数据
     * @param employee
     * @param page
     * @param limit
     * @return
     */
    public List<Employee> selectDataByPage(Employee employee, Integer page, Integer limit) {
        // 开启分页插件
        PageHelper.startPage(page, limit);
        // 使用分页工具查询
        PageInfo<Employee> pageInfo = new PageInfo<Employee>(employeeMapper.selectDataByParam(employee));
        // 返回数据
        return pageInfo.getList();
    }

    /**
     * 查询数据总数
     * @param employee
     * @return
     */
    public Integer selectDataCount(Employee employee) {
        return employeeMapper.selectDataCount(employee);
    }

    /**
     * 根据参数查询员工信息
     * @param employee
     * @return
     */
    public List<Employee> selectDataByParam(Employee employee) {
        return employeeMapper.selectDataByParam(employee);
    }
}
