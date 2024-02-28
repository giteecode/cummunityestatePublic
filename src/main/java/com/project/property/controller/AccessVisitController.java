package com.project.property.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.project.property.entity.AccessVisit;
import com.project.property.entity.ResultMessage;
import com.project.property.service.AccessVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 疫情打卡管理控制器
 */
@RestController
@RequestMapping("/accessVisit")
public class AccessVisitController {

    /**
     * 业务对象
     */
    @Autowired
    private AccessVisitService accessVisitService;

    /**
     * 条件 分页查询  适用于Layui数据表格
     *
     * @param accessVisit 查询条件
     * @param page        当前页
     * @param limit       每页显示的条数
     * @return ResultMessage
     */
    @GetMapping("/getDataByPage")
    public ResultMessage getDataByPage(AccessVisit accessVisit, Integer page, Integer limit) {
        // 查询数据
        try {
            if (StrUtil.isNotBlank(accessVisit.getCreateDate())) {
                String[] split = accessVisit.getCreateDate().split(" - ");
                if (split.length == 2) {
                    accessVisit.setCreateDateStart(DateUtil.parse(split[0], "yyyy-MM-dd HH:mm:ss").toJdkDate());
                    accessVisit.setCreateDateEnd(DateUtil.parse(split[1], "yyyy-MM-dd HH:mm:ss").toJdkDate());
                }
            }

            List<AccessVisit> dataList = accessVisitService.selectDataByPage(accessVisit, page, limit);
            Integer count = accessVisitService.selectCountByParam(accessVisit);
            if (dataList != null && dataList.size() > 0) {
                return new ResultMessage(0, "查询成功！", dataList, count, limit);
            } else {
                return new ResultMessage(1, "暂无相关数据！");
            }
        } catch (Exception e) {
            return new ResultMessage(1, "查询出现异常：" + e.getMessage());
        }
    }

    /**
     * 插入方法
     *
     * @param accessVisit 插入的对象
     * @return ResultMessage
     */
    @PostMapping("insertInfo")
    public ResultMessage insertInfo(@RequestBody AccessVisit accessVisit) {
        try {
            // 执行新增方法
            accessVisit.setCreateDate(DateUtil.now());
            int result = accessVisitService.insertSelective(accessVisit);
            if (result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch (Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }

    /**
     * 删除方法
     *
     * @param ids 要删除的ID, 多个用逗号隔开
     * @return ResultMessage
     */
    @GetMapping("/deleteInfo")
    public ResultMessage deleteInfo(String ids) {
        try {
            // 执行更新方法
            int result = accessVisitService.deleteByPrimaryKey(ids);
            if (result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if (result == -500) {
                return new ResultMessage(207, "操作失败！选择的信息包含未处理信息！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch (Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }
}
