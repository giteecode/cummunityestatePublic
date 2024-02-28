package com.project.property.service;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.property.dao.*;
import com.project.property.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;


@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserUnitRelationMapper unitRelationMapper;

    @Resource
    private HouseInfoMapper houseInfoMapper;

    @Resource
    private CarParkMapper carParkMapper;

    @Resource
    private UserComplaintMapper userComplaintMapper;

    @Resource
    private UserRepairMapper userRepairMapper;

    @Resource
    private PropertyPayVisitMapper propertyPayVisitMapper;

    @Autowired
    private CommentService commentService;

    /**
     * 根据主键删除
     *
     * @param ids
     * @return
     */
    public int deleteByPrimaryKey(String ids) {
        // 查询改用户信息是否被 用户与住房信息绑定， 如果有绑定不可删除
        List<UserUnitRelation> relations = unitRelationMapper.selectInfoByIds(null, ids);
        if (relations != null && relations.size() > 0) {
            return -500;
        }
        // 查询是否有与评价表绑定的信息, 如果有不可以删除
        List<Comment> comments = commentService.selectInfoByUserId(ids);
        if (comments != null && comments.size() > 0) {
            return -500;
        }
        return userMapper.deleteByPrimaryKey(ids);
    }

    /**
     * 全量插入
     *
     * @param record
     * @return
     */
    public int insert(User record) {
        // 判断如果身份证号不为空则查询身份证号信息
        if (!StrUtil.isBlank(record.getCardNum())) {
            // 使用Hutool工具类解析身份证
            // 户籍信息
            record.setRegisterAddress(IdcardUtil.getProvinceByIdCard(record.getCardNum()));
            // 性别
            record.setSex(IdcardUtil.getGenderByIdCard(record.getCardNum()) == 0 ? "女" : "男");
        }
        return userMapper.insert(record);
    }

    /**
     * 选择性插入
     *
     * @param record
     * @return
     */
    public int insertSelective(User record) {
        // 判断如果身份证号不为空则查询身份证号信息
        if (!StrUtil.isBlank(record.getCardNum())) {
            // 使用Hutool工具类解析身份证
            // 户籍信息
            record.setRegisterAddress(IdcardUtil.getProvinceByIdCard(record.getCardNum()));
            // 性别
            record.setSex(IdcardUtil.getGenderByIdCard(record.getCardNum()) == 0 ? "女" : "男");
        }
        // 查询插入的用户信息是否重复
        User selectParam = new User();
        selectParam.setUserName(record.getUserName());
        selectParam.setPhone(record.getPhone());
        List<User> users = userMapper.selectDataByParam(selectParam);
        if (users != null && users.size() > 0) {
            return -500;
        }
        return userMapper.insertSelective(record);
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据主键选择性更新
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(User record) {
        // 判断如果身份证号不为空则查询身份证号信息
        if (!StrUtil.isBlank(record.getCardNum())) {
            // 使用Hutool工具类解析身份证
            // 户籍信息
            record.setRegisterAddress(IdcardUtil.getProvinceByIdCard(record.getCardNum()));
            // 性别
            record.setSex(IdcardUtil.getGenderByIdCard(record.getCardNum()) == 0 ? "女" : "男");
        }
        // 更新前将相关数据更新
        HouseInfo houseInfo = new HouseInfo();
        houseInfo.setUserId(record.getId());
        houseInfo.setOwnerName(record.getUserName());
        houseInfo.setPhone(record.getPhone());
        houseInfoMapper.updateInfoByUserId(houseInfo);
        List<CarPark> carPark = carParkMapper.selectInfoByUserName(record.getUserName());
        if (carPark != null && carPark.size() > 0) {
            for (CarPark park : carPark) {
                park.setOwnerName(record.getUserName());
                park.setPhone(record.getPhone());
                // 调用更新方法
                carParkMapper.updateByPrimaryKeySelective(park);
            }
        }
        UserComplaint userComplaint = new UserComplaint();
        userComplaint.setUserId(record.getId());
        userComplaint.setUserName(record.getUserName());
        userComplaint.setPhone(record.getPhone());
        userComplaintMapper.updateInfoByUserId(userComplaint);
        UserRepair userRepair = new UserRepair();
        userRepair.setUserId(record.getId());
        userRepair.setUserName(record.getUserName());
        userRepair.setPhone(record.getPhone());
        userRepairMapper.updateInfoByUserId(userRepair);
        List<PropertyPayVisit> propertyPayVisit = propertyPayVisitMapper.selectInfoByUserName(record.getUserName());
        if (propertyPayVisit != null && propertyPayVisit.size() > 0) {
            for (PropertyPayVisit payVisit : propertyPayVisit) {
                payVisit.setClientName(record.getUserName());
                propertyPayVisitMapper.updateByPrimaryKeySelective(payVisit);
            }
        }

        return userMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键更新信息
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKey(User record) {
        // 判断如果身份证号不为空则查询身份证号信息
        if (!StrUtil.isBlank(record.getCardNum())) {
            // 使用Hutool工具类解析身份证
            // 户籍信息
            record.setRegisterAddress(IdcardUtil.getProvinceByIdCard(record.getCardNum()));
            // 性别
            record.setSex(IdcardUtil.getGenderByIdCard(record.getCardNum()) == 0 ? "女" : "男");
        }
        return userMapper.updateByPrimaryKey(record);
    }

    /**
     * 分页查询数据
     *
     * @param user  查询条件
     * @param page  当前页
     * @param limit 每页显示条数
     * @return
     */
    public List<User> selectDataByPage(User user, Integer page, Integer limit) {
        // 开启分页插件
        PageHelper.startPage(page, limit);
        // 使用分页工具查询
        PageInfo<User> pageInfo = new PageInfo<User>(userMapper.selectDataByParam(user));
        // 返回数据
        return pageInfo.getList();
    }

    /**
     * 查询数据总量
     *
     * @param user
     * @return
     */
    public Integer selectDataCount(User user) {
        return userMapper.selectDataCount(user);
    }

    /**
     * 根据条件查询
     *
     * @param user
     * @return
     */
    public List<User> selectDataByParam(User user) {
        return userMapper.selectDataByParam(user);
    }

    /**
     * 前台登陆
     *
     * @param user
     * @return
     */
    public User selectLoginByParam(User user) {
        return userMapper.selectLoginByParam(user);
    }

    public User selectByParam2(User user) {
        return userMapper.selectUserByParam(user);
    }
}
