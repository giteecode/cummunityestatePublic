package com.project.property;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.crypto.SecureUtil;
import com.project.property.entity.Admin;
import com.project.property.entity.TpUser;
import com.project.property.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class PropertyAdminApplicationTests {

    @Autowired
    private AdminService adminService;

    @Test
    void contextLoads() {
        // 三重加密
        // 例如用户密码是123456
        // 首先将密码进行SHA1加密
        String password = SecureUtil.sha256("123456");
        // 例如盐值是asdf，将盐值进行SHA256加密
        String salt = UUID.randomUUID().toString();
        System.out.println(salt);
        // 之后将两个加密后的字符串相加再进行MD5加密
        password = SecureUtil.md5(password + SecureUtil.sha1(salt));
        System.out.println(password);
    }

    @Test
    void transactionAdviceConfig() {
        Admin admin = new Admin();
        //admin.setId(000002);
        admin.setLoginName("1000");
        adminService.updateByPrimaryKeySelective(admin);

    }

    @Test
    void testSQL() {
        System.out.println(adminService.selectByPrimaryKey(1));
    }

    @Test
    void testIdCardHandle() {
        String idCard = "11010519491231001X";
        System.out.println(IdcardUtil.getGenderByIdCard(idCard));
        System.out.println(IdcardUtil.getProvinceByIdCard(idCard));
    }

    @Test
    void testDate() {
        System.out.println(DateUtil.month(new Date()));
        String dateStr = "2020-9-25 00:12:01";
        Date date = DateUtil.parse(dateStr, "yyyy-MM-dd HH:mm:ss");
        System.out.println(DateUtil.month(date));
    }

    @Test
    void testDateStr() {
        int month = DateUtil.month(new Date()) + 1;
        String nowStr = DateUtil.today();
        // 将月份替换
        String[] split = nowStr.split("-");
        split[1] = month+"";
        split[2] = "01";
        String readDateStr = split[0] + "-" + split[1] + "-" + split[2] + " 00:00:00";
        System.out.println(readDateStr);
    }

    @Test
    void testSelect() {
        List<Admin> admins = adminService.selectByAdmin(null);
        System.out.println(admins.toString());
//        TpUser tpUser = tpUserService.selectByPrimaryKey(1);
//        System.out.println(tpUser.toString());
    }

}
