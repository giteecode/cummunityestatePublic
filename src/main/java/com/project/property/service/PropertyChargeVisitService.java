package com.project.property.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.property.entity.PropertyChargeItem;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.project.property.entity.PropertyChargeVisit;
import com.project.property.dao.PropertyChargeVisitMapper;

import java.util.Date;
import java.util.List;


@Service
public class PropertyChargeVisitService{

    /**
     * 数据访问层对象
     */
    @Resource
    private PropertyChargeVisitMapper propertyChargeVisitMapper;

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    public int deleteByPrimaryKey(Integer id) {
        return propertyChargeVisitMapper.deleteByPrimaryKey(id);
    }

    /**
     * 全量插入
     * @param record
     * @return
     */
    public int insert(PropertyChargeVisit record) {
        return propertyChargeVisitMapper.insert(record);
    }

    /**
     * 选择性插入
     * @param record
     * @return
     */
    public int insertSelective(PropertyChargeVisit record) {
        // 处理数据
        // 设置缴费项信息
        record.setItemId(Integer.parseInt(record.getItemName().split("\\|")[0]));
        record.setItemName(record.getItemName().split("\\|")[1]);
        // 设置楼宇信息
        record.setHouseId(Integer.parseInt(record.getHouseNum().split("\\|")[0]));
        record.setBuildingNum(record.getHouseNum().split("\\|")[1]);
        record.setUnitNum(record.getHouseNum().split("\\|")[2]);
        record.setHouseNum(record.getHouseNum().split("\\|")[3]);
        // 设置缴费状态， 默认1 未交费
        record.setVisitStatus("1");
        return propertyChargeVisitMapper.insertSelective(record);
    }

    /**
     * 根据主键查询数据
     * @param id
     * @return
     */
    public PropertyChargeVisit selectByPrimaryKey(Integer id) {
        return propertyChargeVisitMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据主键选择性更新
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(PropertyChargeVisit record) {
        // 处理数据
        // 设置缴费项信息
        record.setItemId(Integer.parseInt(record.getItemName().split("\\|")[0]));
        record.setItemName(record.getItemName().split("\\|")[1]);
        // 设置楼宇信息
        record.setHouseId(Integer.parseInt(record.getHouseNum().split("\\|")[0]));
        record.setBuildingNum(record.getHouseNum().split("\\|")[1]);
        record.setUnitNum(record.getHouseNum().split("\\|")[2]);
        record.setHouseNum(record.getHouseNum().split("\\|")[3]);
        // 设置缴费状态， 默认1 未交费
        record.setVisitStatus(record.getVisitStatus());
        return propertyChargeVisitMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    public int updateByPrimaryKey(PropertyChargeVisit record) {
        // 处理数据
        // 设置缴费项信息
        record.setItemId(Integer.parseInt(record.getItemName().split("\\|")[0]));
        record.setItemName(record.getItemName().split("\\|")[1]);
        // 设置楼宇信息
        record.setHouseId(Integer.parseInt(record.getHouseNum().split("\\|")[0]));
        record.setBuildingNum(record.getHouseNum().split("\\|")[1]);
        record.setUnitNum(record.getHouseNum().split("\\|")[2]);
        record.setHouseNum(record.getHouseNum().split("\\|")[3]);
        // 设置缴费状态， 默认1 未交费
        record.setVisitStatus(record.getVisitStatus());
        return propertyChargeVisitMapper.updateByPrimaryKey(record);
    }

    /**
     * 分页查询数据
     * @param propertyChargeVisit   查询条件
     * @param page      当前页
     * @param limit     每页显示的数据量
     * @return
     */
    public List<PropertyChargeVisit> selectDataByPage(PropertyChargeVisit propertyChargeVisit, Integer page, Integer limit) {
        // 开启分页插件
        PageHelper.startPage(page, limit);
        // 查询数据
        PageInfo<PropertyChargeVisit> pageInfo = new PageInfo<PropertyChargeVisit>(propertyChargeVisitMapper.selectDataByParam(propertyChargeVisit));
        // 返回数据
        return pageInfo.getList();
    }

    /**
     * 查询数据总量
     * @param propertyChargeVisit
     * @return
     */
    public Integer selectDataCount(PropertyChargeVisit propertyChargeVisit) {
        return propertyChargeVisitMapper.selectDataCount(propertyChargeVisit);
    }

    /**
     * 根据收费项和房号查询该房间最近一次的缴费记录
     * @param itemId        收费项ID
     * @param houseNum      房间号
     * @return
     */
    public Object selectByItemIdAndHouseNum(Integer itemId, String houseNum) {
        PropertyChargeVisit chargeVisit = propertyChargeVisitMapper.selectByItemIdAndHouseNum(itemId, houseNum);
        // 查询为空直接返回null
        if(chargeVisit == null) {
            return null;
        }
        // 查询不是null，则查看是否是本月抄的表(例如，如果现在是8月，那他上个月抄表月份应该为7月，如果为8月则代表这个月已抄表)
        // 取出抄表时间
        String readDateStr = chargeVisit.getReadDate();
        // 将抄表时间转换为日期对象后取出月份
        Date readDate = DateUtil.parse(readDateStr, "yyyy-MM-dd");
        // 取出月份(取出月份时需要+1，因为月份从0开始)
        int readMonth = DateUtil.month(readDate) + 1;
        // 取出当前时间的月份，判断是否是本月抄表
        int nowMonth = DateUtil.month(new Date()) + 1;
        if(readMonth == nowMonth) {
            // 本月已抄表
            return -500;

        } else {
            return chargeVisit;
        }
    }
}
