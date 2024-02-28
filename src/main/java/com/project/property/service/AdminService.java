package com.project.property.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.property.dao.MenuAdminRelationMapper;
import com.project.property.dao.MenuMapper;
import com.project.property.entity.Menu;
import com.project.property.entity.MenuAdminRelation;
import com.project.property.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.project.property.entity.Admin;
import com.project.property.dao.AdminMapper;

import java.util.List;

/**
 * 添加管理员
 */

@Service
public class AdminService{

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenuAdminRelationMapper adminRelationMapper;

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    public int deleteByPrimaryKey(Integer id) {
        // 删除前判断是否还有管理员，如果这是最后一个管理员则不可删除
        Integer result = adminMapper.selectDataCount(null);
        if(result == 1) {
            return -500;
        }
        return adminMapper.deleteByPrimaryKey(id);
    }

    /**
     * 插入
     * @param record
     * @return
     */
    public int insert(Admin record) {
        Integer result  = -1;
        // 取出用户输入的密码
        String password = record.getPassword();
        // 获取一个UUID作为盐
        String salt = RandomUtil.randomUUID();
        record.setSalt(salt);
        // 加密
        String finalPwd = SecureUtil.md5(SecureUtil.sha1(password) + SecureUtil.sha256(salt));
        record.setPassword(finalPwd);
        result = adminMapper.insert(record);
        List<Menu> menus = menuMapper.selectByAllInfo();
        // 为用户设置菜单
        for (Menu menu : menus) {
            MenuAdminRelation menuAdminRelation = new MenuAdminRelation();
            menuAdminRelation.setUserId(record.getId());
            menuAdminRelation.setMenuId(menu.getId());
            result += adminRelationMapper.insertSelective(menuAdminRelation);
        }
        return result;
    }

    /**
     * 选择性插入
     * @param record
     * @return
     */
    public int insertSelective(Admin record) {
        Integer result  = -1;
        // 取出用户输入的密码
        String password = record.getPassword();
        // 获取一个UUID作为盐
        String salt = RandomUtil.randomUUID();
        record.setSalt(salt);
        // 加密
        String finalPwd = SecureUtil.md5(SecureUtil.sha1(password) + SecureUtil.sha256(salt));
        record.setPassword(finalPwd);
        result = adminMapper.insertSelective(record);
        List<Menu> menus = menuMapper.selectByAllInfo();
        // 为用户设置菜单
        for (Menu menu : menus) {
            MenuAdminRelation menuAdminRelation = new MenuAdminRelation();
            menuAdminRelation.setUserId(record.getId());
            menuAdminRelation.setMenuId(menu.getId());
            result += adminRelationMapper.insertSelective(menuAdminRelation);
        }
        return result;
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    public Admin selectByPrimaryKey(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据主键选择性更新
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(Admin record) {
        // 取出用户输入的密码
        String password = record.getPassword();
        // 获取一个UUID作为盐
        String salt = RandomUtil.randomUUID();
        record.setSalt(salt);
        // 加密
        String finalPwd = SecureUtil.md5(SecureUtil.sha1(password) + SecureUtil.sha256(salt));
        record.setPassword(finalPwd);
        return adminMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    public int updateByPrimaryKey(Admin record) {
        // 取出用户输入的密码
        String password = record.getPassword();
        // 获取一个UUID作为盐
        String salt = RandomUtil.randomUUID();
        record.setSalt(salt);
        // 加密
        String finalPwd = SecureUtil.md5(SecureUtil.sha1(password) + SecureUtil.sha256(salt));
        record.setPassword(finalPwd);
        return adminMapper.updateByPrimaryKey(record);
    }

    /**
     * 根据admin条件查询
     * @param admin 查询条件
     * @return 返回Admin集合
     */
    public List<Admin> selectByAdmin(Admin admin) {
        return adminMapper.selectByAdmin(admin);
    }

    /**
     * 登陆验证
     * @param admin 查询条件
     * @return 返回Admin
     */
    public Admin selectByLogin(Admin admin) {
        // 首先根据用户名查询出该用户
        Admin adminUserName = new Admin();
        adminUserName.setUserName(admin.getUserName());
        List<Admin> admins = adminMapper.selectByAdmin(adminUserName);
        // 如果集合为null代表没有该用户名
        if(admins == null || admins.size() == 0) {
            return null;
        }
        // 循环查出的admin集合, 加密进行匹配密码是否正确
        for (Admin item : admins) {
            // 取出盐值
            String salt = item.getSalt();
            // 取出用户输入的密码
            String password = admin.getPassword();
            // 加密后进行判断   加密规则：MD5(密码sha1加密 + 盐值sha256加密)
            String finalPwd = SecureUtil.md5(SecureUtil.sha1(password) + SecureUtil.sha256(salt));
            // 进行比对, 如果正确则代表登陆成功
            if(item.getPassword().equals(finalPwd)) {
                // 验证通过, 返回登陆对象
                return item;
            }
        }
        return null;
    }

    /**
     * 分页查询数据
     * @param admin     查询条件
     * @param page      当前页
     * @param limit     每页显示数量
     * @return
     */
    public List<Admin> selectDataByPage(Admin admin, Integer page, Integer limit) {
        // 开启分页
        PageHelper.startPage(page, limit);
        // 查询数据, 使用分页
        PageInfo<Admin> pageInfo = new PageInfo<Admin>(adminMapper.selectDataByParam(admin));
        // 返回查询数据
        return pageInfo.getList();
    }

    /**
     * 查询数据总数量
     * @param admin     查询条件
     * @return
     */
    public Integer selectDataCount(Admin admin) {
        return adminMapper.selectDataCount(admin);
    }
}
