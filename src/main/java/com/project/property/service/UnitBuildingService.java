package com.project.property.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.property.dao.HouseInfoMapper;
import com.project.property.entity.HouseInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.project.property.dao.UnitBuildingMapper;
import com.project.property.entity.UnitBuilding;

import java.util.List;


@Service
public class UnitBuildingService{

    @Resource
    private UnitBuildingMapper unitBuildingMapper;

    @Resource
    private HouseInfoMapper houseInfoMapper;

    /**
     * 根据主键批量删除
     * @param ids
     * @return
     */
    public int deleteByPrimaryKey(String  ids) {
        // 查询住房信息
        List<UnitBuilding> unitBuildings = unitBuildingMapper.selectInfoByIds(ids);
        if(unitBuildings==null || unitBuildings.size() == 0) {
            return -1;
        }
        String nums = "";
        for (UnitBuilding unitBuilding : unitBuildings) {
            nums += unitBuilding.getBuildingNum() + ",";
        }
        // 首先判断是否有仍被住房信息引用的信息， 如果有不可删除
        List<HouseInfo> houseInfos = houseInfoMapper.selectInfoByBuildingNum(nums.substring(0, nums.length() - 1));
        if(houseInfos != null && houseInfos.size() > 0) {
            // 代表有被外键引用的信息，不可删除
            return -500;
        }
        // 没有被引用信息，可以删除
        return unitBuildingMapper.deleteByPrimaryKey(ids);
    }

    /**
     * 全量插入
     * @param record
     * @return
     */
    public int insert(UnitBuilding record) {
        // 首先要确定楼宇号不可重复
        UnitBuilding unitBuilding = selectInfoByNum(record.getBuildingNum());
        // 如果不存在则可以进行操作， 否则不可以
        if(unitBuilding == null) {
            return unitBuildingMapper.insert(record);
        }
        return -500;
    }

    /**
     * 选择性插入
     * @param record
     * @return
     */
    public int insertSelective(UnitBuilding record) {
        // 首先要确定楼宇号不可重复
        UnitBuilding unitBuilding = selectInfoByNum(record.getBuildingNum());
        // 如果不存在则可以进行操作， 否则不可以
        if(unitBuilding == null) {
            return unitBuildingMapper.insertSelective(record);
        }
        return -500;
    }

    
    public UnitBuilding selectByPrimaryKey(Integer id) {
        return unitBuildingMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据主键选择性更新方法
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(UnitBuilding record) {
        // 首先要确定楼宇号不可重复
        UnitBuilding unitBuilding = selectInfoByNum(record.getBuildingNum());
        // 如果不存在则可以进行操作， 否则不可以
        if(unitBuilding == null) {
            return unitBuildingMapper.updateByPrimaryKeySelective(record);
        } else if(record.getId() == unitBuilding.getId()) {
            // 如果这两个对象的主键ID相等，但是其他数据不相等，代表是更改了该对象的某些数据，则可以更新
            return unitBuildingMapper.updateByPrimaryKeySelective(record);
        }
        return -500;
    }

    /**
     * 全量更新
     * @param record
     * @return
     */
    public int updateByPrimaryKey(UnitBuilding record) {
        // 首先要确定楼宇号不可重复
        UnitBuilding unitBuilding = selectInfoByNum(record.getBuildingNum());
        // 如果不存在则可以进行操作， 否则不可以
        if(unitBuilding == null) {
            return unitBuildingMapper.updateByPrimaryKeySelective(record);
        } else if(record.getId() == unitBuilding.getId()) {
            // 如果这两个对象的主键ID相等，代表是更改了该对象的某些数据，则可以更新
            return unitBuildingMapper.updateByPrimaryKeySelective(record);
        }
        return -500;
    }

    /**
     * 分页查询数据
     * @param unitBuilding  查询条件
     * @param page          当前页
     * @param limit         每页条数
     * @return
     */
    public List<UnitBuilding> selectDataByPage(UnitBuilding unitBuilding, Integer page, Integer limit) {
        // 开启分页插件
        PageHelper.startPage(page, limit);
        // 查询数据并分页
        PageInfo<UnitBuilding> pageInfo = new PageInfo<UnitBuilding>(unitBuildingMapper.selectDataByParam(unitBuilding));
        // 返回数据
        return pageInfo.getList();
    }

    /**
     * 查询数据总数
     * @param unitBuilding
     * @return
     */
    public Integer selectDataCount(UnitBuilding unitBuilding) {
        // 查询数据总量
        return unitBuildingMapper.selectDataCount(unitBuilding);
    }

    /**
     * 根据条件查询数据
     * @param unitBuilding
     * @return
     */
    public List<UnitBuilding> selectAllInfo(UnitBuilding unitBuilding) {
        return unitBuildingMapper.selectDataByParam(unitBuilding);
    }

    /**
     * 根据楼宇号查询一个楼宇信息
     * @param num 要查询的楼宇号
     * @return
     */
    public UnitBuilding selectInfoByNum(Integer num) {
        return unitBuildingMapper.selectInfoByNum(num);
    }
}
