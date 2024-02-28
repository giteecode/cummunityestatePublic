package com.project.property.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.property.entity.UserRepair;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.project.property.dao.UserUnitRelationMapper;
import com.project.property.entity.UserUnitRelation;

import java.util.List;


@Service
public class UserUnitRelationService{

    @Resource
    private UserUnitRelationMapper userUnitRelationMapper;

    /**
     * 根据主键删除
     * @param ids
     * @return
     */
    public int deleteByPrimaryKey(String ids) {
        // 判断本次删除是否包含户主(如果包含户主则不可删除)
        List<UserUnitRelation> userUnitRelations = userUnitRelationMapper.selectByPrimaryKeys(ids);
        // 循环查找是否有户主
        for (UserUnitRelation userUnitRelation : userUnitRelations) {
            if("1".equals(userUnitRelation.getIsHouseHolder())) {
                return -500;
            }
        }
        return userUnitRelationMapper.deleteByPrimaryKey(ids);
    }

    /**
     * 全量插入
     * @param record
     * @return
     */
    public int insert(UserUnitRelation record) {
        // 插入前判断该用户是否已经绑定在该房间中
        List<UserUnitRelation> userUnitRelations = userUnitRelationMapper.selectInfoByIds(record.getUnitId()+"", record.getUserId()+"");
        if(userUnitRelations != null && userUnitRelations.size() > 0) {
            return -500;
        }
        // 插入前设置时间
        record.setCreateDate(DateUtil.now());
        // 设置为家庭成员
        record.setIsHouseHolder("0");
        return userUnitRelationMapper.insert(record);
    }

    /**
     * 选择性插入
     * @param record
     * @return
     */
    public int insertSelective(UserUnitRelation record) {
        // 插入前判断该用户是否已经绑定在该房间中
        List<UserUnitRelation> userUnitRelations = userUnitRelationMapper.selectInfoByIds(record.getUnitId()+"", record.getUserId()+"");
        if(userUnitRelations != null && userUnitRelations.size() > 0) {
            return -500;
        }
        // 插入前设置时间
        record.setCreateDate(DateUtil.now());
        // 设置为家庭成员
        record.setIsHouseHolder("0");
        return userUnitRelationMapper.insertSelective(record);
    }

    /**
     * 根据主键查询单条数据
     * @param id
     * @return
     */
    public UserUnitRelation selectByPrimaryKey(Integer id) {
        return userUnitRelationMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据主键选择性更新
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(UserUnitRelation record) {
        return userUnitRelationMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    public int updateByPrimaryKey(UserUnitRelation record) {
        return userUnitRelationMapper.updateByPrimaryKey(record);
    }

    /**
     * 分页查询数据
     * @param userUnitRelation
     * @param page
     * @param limit
     * @return
     */
    public List<UserUnitRelation> selectDataByPage(UserUnitRelation userUnitRelation, Integer page, Integer limit) {
        // 开启分页插件
        PageHelper.startPage(page, limit);
        // 使用分页插件
        PageInfo<UserUnitRelation> pageInfo = new PageInfo<UserUnitRelation>(userUnitRelationMapper.selectDataByParam(userUnitRelation));
        // 返回数据
        return pageInfo.getList();
    }

    /**
     * 查询数据总量
     * @param userUnitRelation
     * @return
     */
    public Integer selectDataCount(UserUnitRelation userUnitRelation) {
        return userUnitRelationMapper.selectDataCount(userUnitRelation);
    }
}
