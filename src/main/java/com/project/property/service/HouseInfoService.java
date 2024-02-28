package com.project.property.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.property.dao.HouseInfoMapper;
import com.project.property.dao.UserMapper;
import com.project.property.dao.UserUnitRelationMapper;
import com.project.property.entity.HouseInfo;
import com.project.property.entity.User;
import com.project.property.entity.UserUnitRelation;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class HouseInfoService {

    /**
     * 数据访问层对象
     */
    @Resource
    private HouseInfoMapper houseInfoMapper;

    /**
     * 数据访问层对象
     */
    @Resource
    private UserUnitRelationMapper unitRelationMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 根据主键删除
     *
     * @param ids
     * @return
     */
    public int deleteByPrimaryKey(String ids) {
        // 删除前查询要删除的房间在关系表中是否还有引用
        // 查询关系表
        List<UserUnitRelation> userUnitRelations = unitRelationMapper.selectInfoByIds(ids, null);
        if (userUnitRelations != null && userUnitRelations.size() > 0) {
            return -500;
        }
        return houseInfoMapper.deleteByPrimaryKey(ids);
    }

    /**
     * 全量插入
     *
     * @param record
     * @return
     */
    public int insert(HouseInfo record) {
        // 判断该房号是否已存在(根据楼号、单元号、楼层、房间号进行判断)
        HouseInfo houseInfo = houseInfoMapper.selectByHouseInfo(record.getParentBuilding(), record.getParentUnit(),
                record.getParentFloor(), record.getHouseNum());
        if (houseInfo != null) {
            return -500;
        }
        return houseInfoMapper.insert(record);
    }

    /**
     * 选择性插入
     *
     * @param record
     * @return
     */
    public int insertSelective(HouseInfo record) {
        // 判断该房号是否已存在(根据楼号、单元号、楼层、房间号进行判断)
        HouseInfo houseInfo = houseInfoMapper.selectByHouseInfo(record.getParentBuilding(), record.getParentUnit(),
                record.getParentFloor(), record.getHouseNum());
        if (houseInfo != null) {
            return -500;
        }
        return houseInfoMapper.insertSelective(record);
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    public HouseInfo selectByPrimaryKey(Integer id) {
        return houseInfoMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据主键选择性更新
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(HouseInfo record) {
        // 判断该房号是否已存在(根据楼号、单元号、楼层、房间号进行判断)
        HouseInfo houseInfo = houseInfoMapper.selectByHouseInfo(record.getParentBuilding(), record.getParentUnit(),
                record.getParentFloor(), record.getHouseNum());
        if (houseInfo == null) {
            return houseInfoMapper.updateByPrimaryKeySelective(record);
        } else if (record.getId() == houseInfo.getId()) {
            // 如果这两个对象的主键ID相等，但是其他数据不相等，代表是更改了该对象的某些数据，则可以更新
            return houseInfoMapper.updateByPrimaryKeySelective(record);
        } else {
            return -500;
        }
    }

    /**
     * 根据主键更新
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKey(HouseInfo record) {
        // 判断该房号是否已存在(根据楼号、单元号、楼层、房间号进行判断)
        HouseInfo houseInfo = houseInfoMapper.selectByHouseInfo(record.getParentBuilding(), record.getParentUnit(),
                record.getParentFloor(), record.getHouseNum());
        if (houseInfo == null) {
            return houseInfoMapper.updateByPrimaryKey(record);
        } else if (record.getId() == houseInfo.getId()) {
            // 如果这两个对象的主键ID相等，但是其他数据不相等，代表是更改了该对象的某些数据，则可以更新
            return houseInfoMapper.updateByPrimaryKey(record);
        } else {
            return -500;
        }
    }

    /**
     * 分页查询数据
     *
     * @param houseInfo 查询条件
     * @param page      当前页数
     * @param limit     每页显示的数据量
     * @return
     */
    public List<HouseInfo> selectDataByPage(HouseInfo houseInfo, Integer page, Integer limit) {
        // 开启分页查询插件
        PageHelper.startPage(page, limit);
        // 使用分页插件查询数据
        PageInfo<HouseInfo> pageInfo = new PageInfo<HouseInfo>(houseInfoMapper.selectDataByParam(houseInfo));
        // 返回查询的数据
        return pageInfo.getList();
    }

    /**
     * 查询总数据量
     *
     * @param houseInfo 查询条件
     * @return
     */
    public Integer selectDataCount(HouseInfo houseInfo) {
        return houseInfoMapper.selectDataCount(houseInfo);
    }

    /**
     * 根据条件查询所有信息
     *
     * @param houseInfo
     * @return
     */
    public List<HouseInfo> selectDataAllInfo(HouseInfo houseInfo) {
        return houseInfoMapper.selectDataByParam(houseInfo);
    }

    /**
     * 更新售出信息(添加户主)
     *
     * @param houseInfo
     * @return
     */
    public int updateSoldInfo(HouseInfo houseInfo, String isHouseHolder) {
        // 首先加入一条关联信息到房屋与住户关联表
        UserUnitRelation userUnitRelation = new UserUnitRelation();
        // 首先查询该房间是否已有户主
        UserUnitRelation selectResult = unitRelationMapper.selectInfoByHouseId(houseInfo.getId());
        if (selectResult != null) {
            // 已存在，更新状态(更新户主信息)
            userUnitRelation = selectResult;
            userUnitRelation.setUserId(houseInfo.getUserId());
            userUnitRelation.setCreateDate(DateUtil.now());
            // 更新
            unitRelationMapper.updateByPrimaryKeySelective(userUnitRelation);
        } else {
            // 不存在，新增绑定关系
            userUnitRelation.setUnitId(houseInfo.getId());
            userUnitRelation.setUserId(houseInfo.getUserId());
            userUnitRelation.setCreateDate(DateUtil.now());
            // 第二个参数标识是否是户主，为空代表是，不为空则不是
            if (StrUtil.isBlank(isHouseHolder)) {
                userUnitRelation.setIsHouseHolder("1");
            } else {
                userUnitRelation.setIsHouseHolder("0");
            }
            // 插入信息
            unitRelationMapper.insertSelective(userUnitRelation);
        }
        // 之后更新房屋信息
        // 首先查询到用户信息
        User user = userMapper.selectByPrimaryKey(houseInfo.getUserId());
        houseInfo.setOwnerName(user.getUserName());
        houseInfo.setPhone(user.getPhone());
        // 更新住房信息
        return houseInfoMapper.updateByPrimaryKeySelective(houseInfo);
    }

    /**
     * 房租不在出租
     *
     * @param houseInfo
     * @return
     */
    public int updateUnSoldInfo(HouseInfo houseInfo) {
        // 之后更新房屋信息
        houseInfo.setIsSold(houseInfo.getIsSold());

        // 更新住房出租信息
        return houseInfoMapper.updateSoldInfoByPrimaryKey(houseInfo);
    }

    /**
     * 添加家庭成员
     *
     * @param houseInfo
     * @return
     */
    public int insertFamilyInfo(HouseInfo houseInfo, String isHouseHolder) {
        // 首先加入一条关联信息到房屋与住户关联表
        UserUnitRelation userUnitRelation = new UserUnitRelation();
        // 首先查询该房间是否已有户主
        UserUnitRelation selectResult = unitRelationMapper.selectInfoByHouseId(houseInfo.getId());
        if (selectResult != null) {
            // 已存在，更新状态(更新户主信息)
            userUnitRelation = selectResult;
            userUnitRelation.setUserId(houseInfo.getUserId());
            userUnitRelation.setCreateDate(DateUtil.now());
            // 更新
            unitRelationMapper.updateByPrimaryKeySelective(userUnitRelation);
        } else {
            // 不存在，新增绑定关系
            userUnitRelation.setUnitId(houseInfo.getId());
            userUnitRelation.setUserId(houseInfo.getUserId());
            userUnitRelation.setCreateDate(DateUtil.now());
            // 第二个参数标识是否是户主，为空代表是，不为空则不是
            if (StrUtil.isBlank(isHouseHolder)) {
                userUnitRelation.setIsHouseHolder("1");
            } else {
                userUnitRelation.setIsHouseHolder("0");
            }
            // 插入信息
            unitRelationMapper.insertSelective(userUnitRelation);
        }
        // 之后更新房屋信息
        // 首先查询到用户信息
        User user = userMapper.selectByPrimaryKey(houseInfo.getUserId());
        houseInfo.setOwnerName(user.getUserName());
        houseInfo.setPhone(user.getPhone());
        // 更新住房信息
        return houseInfoMapper.updateByPrimaryKeySelective(houseInfo);
    }
}
