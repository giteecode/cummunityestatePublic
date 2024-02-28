package com.project.property.controller;

import com.project.property.entity.UnitBuilding;
import com.project.property.entity.ResultMessage;
import com.project.property.service.UnitBuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/building")
public class UnitBuildingController {

    /**
     * 业务对象
     */
    @Autowired
    private UnitBuildingService unitBuildingService;

    /**
     * 条件 分页查询  适用于Layui数据表格
     * @param unitBuilding     查询条件
     * @param page      当前页
     * @param limit     每页显示的条数
     * @return ResultMessage
     */
    @GetMapping("/getDataByPage")
    public ResultMessage getDataByPage(UnitBuilding unitBuilding, Integer page, Integer limit) {
        // 调用查询方法
        try {
            List<UnitBuilding> dataList = unitBuildingService.selectDataByPage(unitBuilding, page, limit);
            Integer count = unitBuildingService.selectDataCount(unitBuilding);
            if(dataList != null && dataList.size() > 0) {
                return new ResultMessage(0, "查询成功！", dataList, count, limit);
            } else {
                return new ResultMessage(1, "暂无相关数据！");
            }
        } catch (Exception e) {
            // 出现异常
            return new ResultMessage(1, "查询出现异常！");
        }
    }

    /**
     * 根据传入的楼宇号查询楼宇信息
     * @param unitBuilding     查询条件
     * @return ResultMessage
     */
    @GetMapping("/getInfoByBuildingNum")
    public ResultMessage getInfoByBuildingNum(UnitBuilding unitBuilding) {
        // 调用查询方法
        try {
            List<UnitBuilding> data = unitBuildingService.selectAllInfo(unitBuilding);
            if(data != null && data.size() == 1) {
                return new ResultMessage(0, "查询成功！", data.get(0));
            } else {
                return new ResultMessage(207, "暂无相关数据！");
            }
        } catch (Exception e) {
            // 出现异常
            return new ResultMessage(500, "查询出现异常！");
        }
    }

    /**
     * 更新方法
     * @param unitBuilding 更新的对象
     * @return ResultMessage
     */
    @PutMapping("/updateInfo")
    public ResultMessage updateInfo(@RequestBody UnitBuilding unitBuilding) {
        try {
            // 执行新增方法
            int result = unitBuildingService.updateByPrimaryKeySelective(unitBuilding);
            if(result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if(result == -500) {
                return new ResultMessage(207, "操作失败！楼宇号已存在！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch(Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }

    /**
     * 插入方法
     * @param unitBuilding 插入的对象
     * @return ResultMessage
     */
    @PostMapping("/insertInfo")
    public ResultMessage insertInfo(@RequestBody UnitBuilding unitBuilding) {
        try {
            // 执行新增方法
            int result = unitBuildingService.insertSelective(unitBuilding);
            if(result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if(result == -500) {
                return new ResultMessage(207, "操作失败！楼宇号已存在！");
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
            int result = unitBuildingService.deleteByPrimaryKey(ids);
            if(result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if(result == -500) {
                return new ResultMessage(207, "操作失败！删除的信息中仍有被引用的信息！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch(Exception e) {
            e.printStackTrace();
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }
}
