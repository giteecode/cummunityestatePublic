package com.project.property.controller;

import com.project.property.entity.HouseInfo;
import com.project.property.entity.PropertyChargeItem;
import com.project.property.entity.ResultMessage;
import com.project.property.service.PropertyChargeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/propertyChargeItem")
public class PropertyChargeItemController {

    /**
     * 业务对象
     */
    @Autowired
    private PropertyChargeItemService propertyChargeItemService;

    /**
     * 条件 分页查询  适用于Layui数据表格
     * @param propertyChargeItem     查询条件
     * @param page      当前页
     * @param limit     每页显示的条数
     * @return ResultMessage
     */
    @GetMapping("/getDataByPage")
    public ResultMessage getDataByPage(PropertyChargeItem propertyChargeItem, Integer page, Integer limit) {
        // 查询数据
        try {
            List<PropertyChargeItem> dataList = propertyChargeItemService.selectDataByPage(propertyChargeItem, page, limit);
            Integer count = propertyChargeItemService.selectDataCount(propertyChargeItem);
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
     * 更新方法
     * @param propertyChargeItem 更新的对象
     * @return ResultMessage
     */
    @PutMapping("/updateInfo")
    public ResultMessage updateInfo(@RequestBody PropertyChargeItem propertyChargeItem) {
        try {
            // 执行更新方法
            int result = propertyChargeItemService.updateByPrimaryKeySelective(propertyChargeItem);
            if(result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if(result == -500) {
                return new ResultMessage(207, "抱歉，物业费项目不可改名！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch(Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }

    /**
     * 插入方法
     * @param propertyChargeItem 插入的对象
     * @return ResultMessage
     */
    @PostMapping("/insertInfo")
    public ResultMessage insertInfo(@RequestBody PropertyChargeItem propertyChargeItem) {
        try {
            // 执行更新方法
            int result = propertyChargeItemService.insertSelective(propertyChargeItem);
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
     * 删除方法
     * @param ids 要删除的ID, 多个用逗号隔开
     * @return ResultMessage
     */
    @GetMapping("/deleteInfo")
    public ResultMessage deleteInfo(String ids) {
        try {
            // 执行方法
            int result = propertyChargeItemService.deleteByPrimaryKey(ids);
            if(result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if(result == -500) {
                return new ResultMessage(207, "删除的数据中包含仍被引用的数据！");
            } else if(result == -600) {
                return new ResultMessage(207, "物业费为基础费用不可删除！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch(Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }
}
