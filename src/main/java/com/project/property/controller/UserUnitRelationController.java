package com.project.property.controller;

import com.project.property.entity.UserRepair;
import com.project.property.entity.UserUnitRelation;
import com.project.property.entity.ResultMessage;
import com.project.property.service.UserUnitRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/userUnitRelation")
public class UserUnitRelationController {

    /**
     * 业务对象
     */
    @Autowired
    private UserUnitRelationService userUnitRelationService;

    /**
     * 条件 分页查询  适用于Layui数据表格
     * @param userUnitRelation     查询条件
     * @param page      当前页
     * @param limit     每页显示的条数
     * @return ResultMessage
     */
    @GetMapping("/getDataByPage")
    public ResultMessage getDataByPage(UserUnitRelation userUnitRelation, Integer page, Integer limit) {
        // 查询数据
        try {
            List<UserUnitRelation> dataList = userUnitRelationService.selectDataByPage(userUnitRelation, page, limit);
            Integer count = userUnitRelationService.selectDataCount(userUnitRelation);
            if(dataList != null && dataList.size() > 0) {
                return new ResultMessage(0, "查询成功！", dataList, count, limit);
            } else {
                return new ResultMessage(1, "暂无相关数据！");
            }
        } catch(Exception e) {
            return new ResultMessage(1, "查询出现异常：" + e.getMessage());
        }
    }

    /**
     * 插入方法
     * @param userUnitRelation 插入的对象
     * @return ResultMessage
     */
    @PostMapping("/insertInfo")
    public ResultMessage insertInfo(@RequestBody UserUnitRelation userUnitRelation) {
        try {
            // 执行新增方法
            int result = userUnitRelationService.insertSelective(userUnitRelation);
            if(result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if(result == -500) {
                return new ResultMessage(207, "操作失败！该成员已经在该房间！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch(Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }

    /**
     * 删除方法
     * @param delIds 要删除的ID, 多个用逗号隔开
     * @return ResultMessage
     */
    @GetMapping("/deleteInfo")
    public ResultMessage deleteInfo(String delIds) {
        try {
            // 执行新增方法
            int result = userUnitRelationService.deleteByPrimaryKey(delIds);
            if(result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if(result == -500) {
                return new ResultMessage(207, "操作失败！户主不可删除！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch(Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }
}
