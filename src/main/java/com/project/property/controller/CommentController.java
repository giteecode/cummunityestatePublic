package com.project.property.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.project.property.entity.Comment;
import com.project.property.entity.ResultMessage;
import com.project.property.service.CommentService;
import com.project.property.utils.SocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 评价模块控制器
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    private static Map<String, Map> param = new HashMap<>();

    static {
        // 公告评论表
        Map<String, String> m1 = new HashMap<>();
        m1.put("table", "comment");
        m1.put("contentField", "content");
        // 报修反馈表
        Map<String, String> m2 = new HashMap<>();
        m2.put("table", "user_repair");
        m2.put("contentField", "feedback_msg");
        // 用户投诉表
        Map<String, String> m3 = new HashMap<>();
        m3.put("table", "user_complaint");
        m3.put("contentField", "feedback_msg");

        param.put("1", m1);
        param.put("2", m2);
        param.put("3", m3);
    }

    /**
     * 业务对象
     */
    @Autowired
    private CommentService commentService;

    /**
     * 条件 分页查询  适用于Layui数据表格
     *
     * @param comment 查询条件
     * @param page    当前页
     * @param limit   每页显示的条数
     * @return ResultMessage
     */
    @GetMapping("/getDataByPage")
    public ResultMessage getDataByPage(Comment comment, Integer page, Integer limit, String type) {
        // 查询数据
        try {
            List<Comment> dataList = commentService.selectDataByPage(comment, page, limit, type);
            Integer count = commentService.selectDataCount(comment, type);
            if (dataList != null && dataList.size() > 0) {
                return new ResultMessage(0, "查询成功！", dataList, count, limit);
            } else {
                return new ResultMessage(1, "暂无相关数据！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(1, "查询出现异常：" + e.getMessage());
        }
    }

    /**
     * 更新方法
     *
     * @param comment 更新的对象
     * @return ResultMessage
     */
    @PutMapping("updateInfo")
    public ResultMessage updateInfo(@RequestBody Comment comment) {
        try {
            // 执行更新方法
            comment.setCreateDate(DateUtil.now());
            int result = commentService.updateByPrimaryKeySelective(comment);
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
     * 插入方法
     *
     * @param comment 插入的对象
     * @return ResultMessage
     */
    @PostMapping("insertInfo")
    public ResultMessage insertInfo(@RequestBody Comment comment) {
        try {
            // 执行新增方法
            comment.setCreateDate(DateUtil.now());
            int result = commentService.insertSelective(comment);
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
     * @param delIds 要删除的ID, 多个用逗号隔开
     * @return ResultMessage
     */
    @GetMapping("/deleteInfo")
    public ResultMessage deleteInfo(String delIds, String type) {
        try {
            // 执行新增方法
            int result = commentService.updateByDelete(delIds, type);
            if (result > 0) {
                return new ResultMessage(0, "操作成功！");
            } else if (result == -500) {
                return new ResultMessage(207, "操作失败！删除的信息中仍有被引用的信息！");
            } else {
                return new ResultMessage(207, "操作失败！请稍后重试！");
            }
        } catch (Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }

    /**
     * @param type 情感分析（调用 Python）
     * @return ResultMessage
     */
    @PostMapping("emotion/analysis")
    public ResultMessage analysisComment(String type, HttpServletRequest request) {
        if (!param.containsKey(type)) {
            return new ResultMessage(400, "请求参数有误");
        }

        try {
            // 通过发起 Socket 请求调用
            SocketClient client = new SocketClient(request.getServerName(), 8089);

            client.sendRequest(JSONUtil.toJsonStr(param.get(type)));

            String receive = client.receive();
//            System.out.println(receive);
            client.close();

            return new ResultMessage(0, receive);

        } catch (Exception e) {
            return new ResultMessage(500, "操作出现异常：" + e.getMessage());
        }
    }

    /**
     * 数据分析统计接口
     *
     * @return
     */
    @GetMapping("/statistic")
    public ResultMessage getDataByPage2() {
        // 查询数据
        try {
            Map dataList = commentService.statisticAnalysis();

            if (ObjectUtil.isNotEmpty(dataList)) {
                return new ResultMessage(0, "查询成功！", dataList, null, null);
            } else {
                return new ResultMessage(1, "暂无数据可以分析！");
            }
        } catch (Exception e) {
            return new ResultMessage(1, "查询出现异常：" + e.getMessage());
        }
    }
}
