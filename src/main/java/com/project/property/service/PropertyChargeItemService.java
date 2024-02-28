package com.project.property.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.property.dao.PropertyChargeVisitMapper;
import com.project.property.entity.PropertyChargeVisit;
import org.apache.xmlbeans.impl.jam.JParameter;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.project.property.entity.PropertyChargeItem;
import com.project.property.dao.PropertyChargeItemMapper;
import com.admin.util.DateUtils;

import java.util.List;


@Service
public class PropertyChargeItemService{

    /**
     * 数据访问层对象
     */
    @Resource
    private PropertyChargeItemMapper propertyChargeItemMapper;

    @Resource
    private PropertyChargeVisitMapper propertyChargeVisitMapper;

    /**
     * 根据主键删除
     * @param ids
     * @return
     */
    public int deleteByPrimaryKey(String ids) {
        // 查询该收费项是否被收费记录绑定, 如果仍有被绑定数据， 则不可删除
        List<PropertyChargeVisit> propertyChargeVisits = propertyChargeVisitMapper.selectInfoByIds(ids);
        if(propertyChargeVisits != null && propertyChargeVisits.size() > 0) {
            return -500;
        }
        // 查询所有要删除的数据：物业费为基础费用，不可删除
        List<PropertyChargeItem> items = propertyChargeItemMapper.selectInfoByIds(ids);
        for (PropertyChargeItem item : items) {
            // 判断是否有物业费
            if(item.getChargeName().indexOf("物业费") > -1) {
                return -600;
            }
        }
        return propertyChargeItemMapper.deleteByPrimaryKey(ids);
    }

    /**
     * 全量插入
     * @param record
     * @return
     */
    public int insert(PropertyChargeItem record) {
        // 设置创建时间时间为当前时间
        record.setCreateDate(DateUtils.getCurrentDateTime());
        // 设置价格为分
        record.setItemPrice(record.getItemPrice() * 100);
        return propertyChargeItemMapper.insert(record);
    }

    /**
     * 选择性插入
     * @param record
     * @return
     */
    public int insertSelective(PropertyChargeItem record) {
        // 设置创建时间时间为当前时间
        record.setCreateDate(DateUtils.getCurrentDateTime());
        // 设置价格为分
        record.setItemPrice(record.getItemPrice() * 100);
        return propertyChargeItemMapper.insertSelective(record);
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    public PropertyChargeItem selectByPrimaryKey(Integer id) {
        return propertyChargeItemMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据主键选择性更新
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(PropertyChargeItem record) {
        // 判断物业费的收费项目名不可修改
        if(!"物业费".equals(record.getChargeName()) && record.getId() == 1) {
            // 如果ID为1并且 收费项名不是物业费，则直接失败
            return -500;
        }
        return propertyChargeItemMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    public int updateByPrimaryKey(PropertyChargeItem record) {
        // 判断物业费的收费项目名不可修改
        if(!"物业费".equals(record.getChargeName()) && record.getId() == 1) {
            // 如果ID为1并且 收费项名不是物业费，则直接失败
            return -500;
        }
        return propertyChargeItemMapper.updateByPrimaryKey(record);
    }

    /**
     * 分页查询数据
     * @param propertyChargeItem
     * @param page
     * @param limit
     * @return
     */
    public List<PropertyChargeItem> selectDataByPage(PropertyChargeItem propertyChargeItem, Integer page, Integer limit) {
        // 开启分页插件
        PageHelper.startPage(page, limit);
        // 查询数据
        PageInfo<PropertyChargeItem> pageInfo = new PageInfo<PropertyChargeItem>(propertyChargeItemMapper.selectDataByParam(propertyChargeItem));
        // 返回分页数据
        return pageInfo.getList();
    }

    /**
     * 查询数据总数量
     * @param propertyChargeItem
     * @return
     */
    public Integer selectDataCount(PropertyChargeItem propertyChargeItem) {
        return propertyChargeItemMapper.selectDataCount(propertyChargeItem);
    }

    /**
     * 根据条件查询全部信息，不分页
     * @param param
     * @return
     */
    public List<PropertyChargeItem> selectDataAllInfo(PropertyChargeItem param) {
        return propertyChargeItemMapper.selectDataByParam(param);
    }
}
