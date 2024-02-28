package com.project.property.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.project.property.dao.NoticeMapper;
import com.project.property.entity.Notice;

import java.util.List;

/**
 * 对应数据库公告表的业务层
 */
@Service
public class NoticeService{

    @Resource
    private NoticeMapper noticeMapper;

    
    public int deleteByPrimaryKey(String ids) {
        return noticeMapper.deleteByPrimaryKey(ids);
    }

    
    public int insertSelective(Notice record) {
        return noticeMapper.insertSelective(record);
    }

    
    public Notice selectByPrimaryKey(Integer id) {
        return noticeMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(Notice record) {
        return noticeMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 分页查询数据
     * @param notice
     * @param page
     * @param limit
     * @return
     */
    public List<Notice> selectDataByPage(Notice notice, Integer page, Integer limit) {
        // 开启分页
        PageHelper.startPage(page, limit);
        PageInfo<Notice> pageInfo = new PageInfo<Notice>(noticeMapper.selectDataByParam(notice));
        return pageInfo.getList();
    }

    /**
     * 查询数据总条数
     * @param notice
     * @return
     */
    public Integer selectDataCount(Notice notice) {
        return noticeMapper.selectCount(notice);
    }
}
