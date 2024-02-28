package com.project.property.controller;

import com.project.property.entity.ResultMessage;
import com.project.property.entity.Employee;
import com.project.property.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/employee")
public class EmployeeController {

    /**
     * 业务对象
     */
    @Autowired
    private EmployeeService employeeService;

    /**
     * 条件 分页查询  适用于Layui数据表格
     * @param employee     查询条件
     * @param page      当前页
     * @param limit     每页显示的条数
     * @return ResultMessage
     */
    @GetMapping("/getDataByPage")
    public ResultMessage getDataByPage(Employee employee, Integer page, Integer limit) {
        // 查询数据
        try {
            List<Employee> dataList = employeeService.selectDataByPage(employee, page, limit);
            Integer count = employeeService.selectDataCount(employee);
            if(dataList != null && dataList.size() > 0) {
                return new ResultMessage(0, "查询成功！", dataList, count, limit);
            } else {
                return new ResultMessage(1, "暂无相关数据！");
            }
        } catch(Exception e) {
            e.printStackTrace();
            return new ResultMessage(1, "查询出现异常：" + e.getMessage());
        }
    }

    /**
     * 更新方法
     * @param employee 更新的对象
     * @return ResultMessage
     */
    @PutMapping("updateInfo")
    public ResultMessage updateInfo(@RequestBody Employee employee) {
        try {
            // 执行更新方法
            int result = employeeService.updateByPrimaryKeySelective(employee);
            if(result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch(Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }

    /**
     * 插入方法
     * @param employee 插入的对象
     * @return ResultMessage
     */
    @PostMapping("insertInfo")
    public ResultMessage insertInfo(@RequestBody Employee employee) {
        try {
            // 执行新增方法
            int result = employeeService.insertSelective(employee);
            if(result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if (result == -500) {
                return new ResultMessage(207, "录入的信息已存在！不可重复录入！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch(Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }

    /**
     * 删除方法
     * @param ids 要删除的ID, 多个用逗号隔开
     * @return ResultMessage
     */
    @GetMapping("/deleteInfo")
    public ResultMessage deleteInfo(String ids) {
        try {
            // 执行新增方法
            int result = employeeService.deleteByPrimaryKey(ids);
            if(result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if(result == -500) {
                return new ResultMessage(207, "操作失败！删除的信息中仍有被引用的信息！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch(Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }
}
