package com.project.property.entity;

import lombok.Data;

/**
 * @Commit 用于返回数据的实体类
 */
@Data
public class ResultMessage {

    /**
     * 返回的消息状态码,
     * 200 成功 (如果是layui数据表格则返回 0代表成功  1代表失败)
     * 207 成功, 后台无报错, 但是数据没有发生改变
     * 400 参数有误
     * 500 后台报错
     */
    private Integer code;

    /**
     * 返回的提示消息
     */
    private String msg;

    /**
     * 返回的数据
     */
    private Object data;

    /**
     * 针对Layui数据表格, 返回数据数量
     */
    private Integer count;

    /**
     * 针对Layui数据表格, 返回每页行数
     */
    private Integer limit;

    /**
     * 无参构造
     */
    public ResultMessage() {

    }

    /**
     * 包含状态码, 消息提示的构造
     * @param code
     * @param msg
     */
    public ResultMessage(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 包含状态码, 消息提示和数据的构造方法
     * @param code
     * @param msg
     * @param data
     */
    public ResultMessage(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 全参构造
     * @param code
     * @param msg
     * @param data
     */
    public ResultMessage(Integer code, String msg, Object data, Integer count) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.count = count;
    }

    /**
     * 全参构造
     * @param code
     * @param msg
     * @param data
     */
    public ResultMessage(Integer code, String msg, Object data, Integer count, Integer limit) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.count = count;
        this.limit = limit;
    }
}
