package com.project.property.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.project.property.entity.PropertyChargeItem;
import com.project.property.entity.PropertyChargeVisit;
import com.project.property.entity.ResultMessage;
import com.project.property.entity.User;
import com.project.property.service.PropertyChargeVisitService;
import com.project.property.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/propertyChargeVisit")
public class PropertyChargeVisitController {

    /**
     * 业务对象
     */
    @Autowired
    private PropertyChargeVisitService propertyChargeVisitService;

    @Autowired
    private UserService userService;

    @Autowired
    private MailAccount mailAccount;

    /**
     * 条件 分页查询  适用于Layui数据表格
     * @param propertyChargeVisit     查询条件
     * @param page      当前页
     * @param limit     每页显示的条数
     * @return ResultMessage
     */
    @GetMapping("/getDataByPage")
    public ResultMessage getDataByPage(PropertyChargeVisit propertyChargeVisit, Integer page, Integer limit) {
        // 查询数据
        try {
            List<PropertyChargeVisit> dataList = propertyChargeVisitService.selectDataByPage(propertyChargeVisit, page, limit);
            Integer count = propertyChargeVisitService.selectDataCount(propertyChargeVisit);
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
     * @param propertyChargeVisit 更新的对象
     * @return ResultMessage
     */
    @PutMapping("/updateInfo")
    public ResultMessage updateInfo(@RequestBody PropertyChargeVisit propertyChargeVisit) {
        try {
            // 执行方法
            int result = propertyChargeVisitService.updateByPrimaryKeySelective(propertyChargeVisit);
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
     * @param propertyChargeVisit 插入的对象
     * @return ResultMessage
     */
    @PostMapping("/insertInfo")
    public ResultMessage insertInfo(@RequestBody PropertyChargeVisit propertyChargeVisit) {
        try {
            // 执行方法
            int result = propertyChargeVisitService.insertSelective(propertyChargeVisit);
            if(result > 0) {
                // 向用户发送缴费邮件  根据用户名和手机号查询到该用户的信息
                User user = new User();
                user.setUserName(propertyChargeVisit.getUserName());
                user.setPhone(propertyChargeVisit.getPhone());
                User findResult = userService.selectByParam2(user);
                System.out.println("发送邮件中...");
                // 发送邮件
                ArrayList<String> target = CollUtil.newArrayList(findResult.getEmail());
                MailUtil.send(mailAccount, target, "小区物业",
                        "[小区物业] 业主您好,您本月需要缴纳的账单已结出,请及时到物业处缴纳,感谢配合！", true);
                System.out.println("邮件已发送! ");
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
        return null;
    }
}
