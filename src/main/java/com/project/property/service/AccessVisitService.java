package com.project.property.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.property.entity.Notice;
import com.project.property.entity.UserComplaint;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.project.property.entity.AccessVisit;
import com.project.property.dao.AccessVisitMapper;

import java.util.List;

/**
 * 出入登记表业务层
 */
@Service
public class AccessVisitService{

    @Resource
    private AccessVisitMapper accessVisitMapper;

    /**
     * 根据主键删除
     * @param ids
     * @return
     */
    public int deleteByPrimaryKey(String ids) {
        return accessVisitMapper.deleteByPrimaryKey(ids);
    }

    /**
     * 选择性插入
     * @param record
     * @return
     */
    public int insertSelective(AccessVisit record) {
        return accessVisitMapper.insertSelective(record);
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    public AccessVisit selectByPrimaryKey(Integer id) {
        return accessVisitMapper.selectByPrimaryKey(id);
    }

    /**
     * 选择性更新
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(AccessVisit record) {
        return accessVisitMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据实体类作为参数查询
     * @param accessVisit
     * @return
     */
    public List<AccessVisit> selectByParam(AccessVisit accessVisit) {
        return accessVisitMapper.selectByParam(accessVisit);
    }

    /**
     * 根据实体类作为参数查询数据总数
     * @param accessVisit
     * @return
     */
    public Integer selectCountByParam(AccessVisit accessVisit) {
        return accessVisitMapper.selectCountByParam(accessVisit);
    }

    /**
     * 分页查询
     * @param accessVisit
     * @param page
     * @param limit
     * @return
     */
    public List<AccessVisit> selectDataByPage(AccessVisit accessVisit, Integer page, Integer limit) {
        // 开启分页插件
        PageHelper.startPage(page, limit);
        // 使用分页插件处理数据
        PageInfo<AccessVisit> pageInfo = new PageInfo<AccessVisit>(accessVisitMapper.selectByParam(accessVisit));
        // 返回数据
        return pageInfo.getList();
    }
}
