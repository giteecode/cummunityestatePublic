package com.project.property.controller;

import cn.hutool.core.util.StrUtil;
import com.project.property.entity.CarPark;
import com.project.property.entity.HouseInfo;
import com.project.property.entity.PropertyChargeVisit;
import com.project.property.entity.ResultMessage;
import com.project.property.service.HouseInfoService;
import com.project.property.service.PropertyChargeVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/house")
public class HouseInfoController {

    /**
     * 业务对象
     */
    @Autowired
    private HouseInfoService houseInfoService;

    @Autowired
    private PropertyChargeVisitService visitService;

    /**
     * 条件 分页查询  适用于Layui数据表格
     *
     * @param houseInfo 查询条件
     * @param page      当前页
     * @param limit     每页显示的条数
     * @return ResultMessage
     */
    @GetMapping("/getDataByPage")
    public ResultMessage getDataByPage(HouseInfo houseInfo, Integer page, Integer limit) {
        // 查询数据
        try {
            List<HouseInfo> dataList = houseInfoService.selectDataByPage(houseInfo, page, limit);
            Integer count = houseInfoService.selectDataCount(houseInfo);
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
     * 根据主键查询
     *
     * @param id 查询条件
     * @return ResultMessage
     */
    @GetMapping("/getInfoById")
    public ResultMessage getInfoById(Integer id, Integer itemId) {
        // 查询数据
        Map<String, Object> resultMap = new HashMap<>(16);
        try {
            // 先查询房间信息
            HouseInfo houseInfo = houseInfoService.selectByPrimaryKey(id);
            // 判断传来的收费项ID是否为空
            if (!StrUtil.isBlank(itemId + "")) {
                // 不为空再根据查出的房间号和收费项ID查出该房间交过的收费记录（需要获取到上个月的缴费记录，也就是最近一次的，从而获取到上月读数）
                // 如果查询结果为null代表该房间还没缴过费，则上月读数为0
                // 如果查询结果的时间为本月，则代表该房间在本月已经抄表，则不能再进行抄表操作
                Object selectObj = visitService.selectByItemIdAndHouseNum(itemId, houseInfo.getHouseNum());
                PropertyChargeVisit chargeVisit = null;
                // 如果返回的是PropertyChargeVisit对象，代表可以抄表
                // 返回为null代表上月读数是0
                // 返回为-1代表本月已抄表
                if (selectObj == null) {
                    chargeVisit = new PropertyChargeVisit();
                    chargeVisit.setPrevMonthCount(0);
                    chargeVisit.setCurrMonthCount(0);
                    resultMap.put("houseInfo", houseInfo);
                    resultMap.put("chargeVisit", chargeVisit);
                    return new ResultMessage(0, "查询成功！", resultMap);
                } else if (selectObj instanceof PropertyChargeVisit) {
                    chargeVisit = (PropertyChargeVisit) selectObj;
                    resultMap.put("houseInfo", houseInfo);
                    resultMap.put("chargeVisit", chargeVisit);
                    return new ResultMessage(0, "查询成功！", resultMap);
                } else if (selectObj instanceof Integer && Integer.parseInt(selectObj.toString()) == -500) {
                    return new ResultMessage(207, "该房间用户本月已录入缴费信息！");
                }
            }
            if (houseInfo != null) {
                resultMap.put("houseInfo", houseInfo);
                return new ResultMessage(0, "查询成功！", resultMap);
            } else {
                return new ResultMessage(207, "暂无相关数据！");
            }
        } catch (Exception e) {
            return new ResultMessage(500, "查询出现异常：" + e.getMessage());
        }
    }

    /**
     * 更新方法
     *
     * @param houseInfo 更新的对象
     * @return ResultMessage
     */
    @PutMapping("/updateInfo")
    public ResultMessage updateInfo(@RequestBody HouseInfo houseInfo) {
        try {
            // 执行更新方法
            int result = houseInfoService.updateByPrimaryKeySelective(houseInfo);
            if (result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if (result == -500) {
                return new ResultMessage(207, "操作失败！该房间号已存在！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch (Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }

    /**
     * 更新方法
     *
     * @param houseInfo 更新的对象
     * @return ResultMessage
     */
    @PutMapping("/soldInfo")
    public ResultMessage soldInfo(@RequestBody HouseInfo houseInfo) {
        try {
            // 执行更新方法
            int result = houseInfoService.updateSoldInfo(houseInfo, null);
            if (result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if (result == -500) {
                return new ResultMessage(207, "操作失败！该房间号已存在！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch (Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }

    /**
     * 房屋不在出租
     *
     * @param houseInfo 更新的对象
     * @return ResultMessage
     */
    @PutMapping("/unsoldInfo")
    public ResultMessage unsoldInfo(@RequestBody HouseInfo houseInfo) {
        try {
            // 执行更新方法
            int result = houseInfoService.updateUnSoldInfo(houseInfo);
            if (result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if (result == -500) {
                return new ResultMessage(207, "操作失败！该房间号已存在！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch (Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }

    /**
     * 插入方法
     *
     * @param houseInfo 插入的对象
     * @return ResultMessage
     */
    @PostMapping("/insertInfo")
    public ResultMessage insertInfo(@RequestBody HouseInfo houseInfo) {
        try {
            // 执行更新方法
            int result = houseInfoService.insertSelective(houseInfo);
            if (result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if (result == -500) {
                return new ResultMessage(207, "操作失败！该房间号已存在！");
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
            int result = houseInfoService.deleteByPrimaryKey(ids);
            if (result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if (result == -500) {
                return new ResultMessage(207, "操作失败！该房间仍被引用！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch (Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }
}
