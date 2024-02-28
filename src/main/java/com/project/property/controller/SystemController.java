package com.project.property.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.project.property.entity.*;
import com.project.property.service.*;
import com.project.property.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

/**
 * 控制页面跳转的控制器
 */
@Controller
@RequestMapping
public class SystemController {

    /**
     * 需要用到的业务对象
     */
    @Autowired
    private AdminService adminService;

    @Autowired
    private MenuAdminRelationService menuAdminRelationService;

    @Autowired
    private CarParkService carParkService;

    @Autowired
    private HouseInfoService houseInfoService;

    @Autowired
    private PropertyChargeItemService propertyChargeItemService;

    @Autowired
    private PropertyChargeVisitService propertyChargeVisitService;

    @Autowired
    private PropertyPayVisitService propertyPayVisitService;

    @Autowired
    private UnitBuildingService unitBuildingService;

    @Autowired
    private UserComplaintService userComplaintService;

    @Autowired
    private UserRepairService userRepairService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserUnitRelationService userUnitRelationService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * 跳转到登录页
     *
     * @return
     */
    @GetMapping(value = {"/system/toLoginPage", "/admin"})
    public String toLoginPage() {
        return "login";
    }

    /**
     * 跳转到登陆过期页面
     *
     * @return
     */
    @GetMapping("/system/toTimeOutPage")
    public String toTimeOutPage() {
        return "system/timeOut";
    }

    /**
     * 跳转到主页
     *
     * @return
     */
    @RequestMapping("/system/toMainPage")
    public String toMainPage(Admin admin, Model model, HttpSession session) {
        // 调用登陆方法
        Admin resultAdmin = adminService.selectByLogin(admin);
        if (resultAdmin == null) {
            model.addAttribute("msg", "密码或账户有误！登录失败！");
            return "login";
        }
        session.setAttribute("loginAdmin", resultAdmin);
        // 查询该用户所拥有的菜单列表
        List<MenuAdminRelation> menuInfo = menuAdminRelationService.selectInfoByUserId(resultAdmin.getId() + "");
        // 将菜单处理, 二级菜单与一级菜单分别存放
        List<MenuAdminRelation> parentMenu = new ArrayList<>(16);
        List<MenuAdminRelation> subMenu = new ArrayList<>(16);
        for (MenuAdminRelation menu : menuInfo) {
            // 如果parent_id为null表示是一级菜单
            if (StringUtils.isNull(menu.getMenu().getParentId() + "")) {
                parentMenu.add(menu);
            } else {
                // 否则是二级菜单
                subMenu.add(menu);
            }
        }
        System.out.println("该管理员拥有的一级菜单" + parentMenu.toString());
        System.out.println("该管理员拥有的二级菜单" + subMenu.toString());
        // 存放菜单信息
        session.setAttribute("parentMenu", parentMenu);
        session.setAttribute("subMenu", subMenu);
        // 会话过期设置为3小时
        session.setMaxInactiveInterval(60 * 60 * 3);
        return "redirect:/main.html";
    }

    /**
     * 退出登录
     *
     * @return
     */
    @GetMapping("/system/loginOut")
    public String loginOut(HttpSession session) {
        session.invalidate();
        return "redirect:/system/toLoginPage";
    }

    /**
     * 跳转到欢迎页
     *
     * @return
     */
    @GetMapping("/system/toWelcomePage")
    public String toWelcomePage() {
        return "system/welcome";
    }

    /**
     * 跳转到上传文件页面
     *
     * @return
     */
    @RequestMapping("/system/toUploadImagePage")
    public String toUploadImagePage() {
        return "system/uploadImagePage";
    }

    /**
     * 上传到服务器
     *
     * @param file
     * @return
     */
    @RequestMapping("/system/uploadImages")
    @ResponseBody
    public ResultMessage toUploadImagesOverride(@RequestParam("file") MultipartFile file, String imgName,
                                                HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String fileName = "";
        try {
            // 设置文件存放路径
            File pathRoot = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!pathRoot.exists()) {
                pathRoot = new File("");
            }
            String path = ResourceUtils.getURL("classpath:").getPath().replace("%20", " ") + "/static/upload/";
            System.out.println("=============path==============：" + path);
            // 判断文件是否为空
            System.out.println(file.getSize());
            if (file.isEmpty() || file.getSize() <= 0) {
                return new ResultMessage(207, "上传文件为空！");
            }
            // 获取上传文件的名字
            fileName = imgName + ".jpg";
            // 创建文件
            File dest = new File(path + fileName);
            result.put("imgName", fileName);
            // 检测目录是否存在
            if (!dest.getParentFile().exists()) {
                // 不存在就创建
                dest.getParentFile().mkdir();
            }
            // 文件写入
            file.transferTo(dest);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(500, "出现异常：" + e.getMessage());
        }
        return new ResultMessage(0, "上传成功！", result);
    }

    // | -------------------------------------------------------------------------------
    // |                             前台所有的页面跳转写这里 Start
    // | -------------------------------------------------------------------------------

    /**
     * 跳转首页
     *
     * @param modelAndView
     * @return
     */
    @GetMapping(value = {"/webPage/index", "/", "/index"})
    public ModelAndView toIndex(ModelAndView modelAndView) {
        List<Notice> noticeInfo = noticeService.selectDataByPage(null, 1, 4);
        Integer noticeCount = noticeService.selectDataCount(null);
        // 计算总页数
        Integer pages = noticeCount % 4 == 0 ? noticeCount / 4 : noticeCount / 4 + 1;
        List<Integer> pagesList = new ArrayList<>(16);
        for (Integer i = 0; i < pages; i++) {
            pagesList.add(i + 1);
        }
        modelAndView.addObject("noticeInfo", noticeInfo);
        modelAndView.addObject("pagesList", pagesList);
        modelAndView.addObject("currPage", 1);
        modelAndView.addObject("totalPage", pages);
        modelAndView.setViewName("webPage/index");
        return modelAndView;
    }

    /**
     * 跳转首页
     *
     * @param modelAndView
     * @return
     */
    @GetMapping("/webPage/loginPage")
    public ModelAndView toWebLoginPage(ModelAndView modelAndView) {
        modelAndView.setViewName("webPage/webLogin");
        return modelAndView;
    }

    /**
     * 跳转我的报修页面
     *
     * @param modelAndView
     * @return
     */
    @GetMapping("/webPage/toRepairPage")
    public ModelAndView toRepairPage(ModelAndView modelAndView, String userId) {
        modelAndView.addObject("userId", userId);
        modelAndView.setViewName("webPage/userRepairPage");
        return modelAndView;
    }

    /**
     * 跳转我的投诉页面
     *
     * @param modelAndView
     * @return
     */
    @GetMapping("/webPage/toComplaintPage")
    public ModelAndView toComplaintPage(ModelAndView modelAndView, String userId) {
        modelAndView.addObject("userId", userId);
        modelAndView.setViewName("webPage/userComplaintPage");
        return modelAndView;
    }

    /**
     * 前台登录
     */
    @PostMapping("/webPage/webLogin")
    @ResponseBody
    public ResultMessage toWebLogin(@RequestBody User user, HttpServletRequest request) {
        // 根据手机号和用户名查询用户信息
        // 查询
        User userResult = userService.selectLoginByParam(user);
        if (userResult == null || userResult.getId() == null) {
            // 输入的信息有误
            return new ResultMessage(500, "登录失败！输入的信息不存在！");
        } else {
            request.getSession().setAttribute("webUser", userResult);
            return new ResultMessage(0, "登录成功！");
        }
    }

    /**
     * 前台退出
     */
    @GetMapping("/webPage/webLoginOut")
    public String toWebLoginOut(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }

    /**
     * 跳转到前台住房管理
     *
     * @param model
     * @return
     */
    @GetMapping("/house/toWebHousePage")
    public ModelAndView toHousePage(ModelAndView model, Integer userId) {
        // 查询楼宇信息作为筛选条件
        List<UnitBuilding> buildingList = unitBuildingService.selectAllInfo(null);
        Integer maxCount = -1;
        // 循环查出最大单元数
        for (UnitBuilding unitBuilding : buildingList) {
            Integer unitCount = unitBuilding.getUnitCount();
            if (maxCount == -1) {
                maxCount = unitCount;
            } else if (maxCount < unitCount) {
                maxCount = unitCount;
            }
        }
        // 该用户的信息
        model.addObject("userId", userId);
        model.addObject("buildingList", buildingList);
        model.addObject("maxCount", maxCount);
        model.setViewName("webPage/houseInfoPage");
        return model;
    }

    /**
     * 跳转到前台购买车位
     *
     * @param model
     * @return
     */
    @GetMapping("/carPark/toWebCarParkPage")
    public ModelAndView toCarParkPage(ModelAndView model, Integer userId) {
        // 该用户的信息
        model.addObject("userId", userId);
        model.setViewName("webPage/carParkPage");
        return model;
    }

    /**
     * 跳转到住房出售页面
     *
     * @return
     */
    @GetMapping("/house/toWebBuyPage")
    public ModelAndView toHouseWebBuyPage(ModelAndView model, Integer id, Integer userId) {
        model.addObject("addUrl", "soldInfo");
        model.addObject("obj", houseInfoService.selectByPrimaryKey(id));
        model.addObject("userInfo", userService.selectByPrimaryKey(userId));
        model.setViewName("webPage/houseSolePage");
        return model;
    }

    /**
     * 跳转到车位出售页面
     *
     * @return
     */
    @GetMapping("/carPark/toWebBuyPage")
    public ModelAndView toCarParkWebBuyPage(ModelAndView model, Integer id, Integer userId) {
        // 根据ID查询车位信息
        CarPark carPark = carParkService.selectByPrimaryKey(id);
        model.addObject("addUrl", "updateInfo");
        model.addObject("obj", carPark);
        model.addObject("userInfo", userService.selectByPrimaryKey(userId));
        model.setViewName("webPage/carParkSolePage");
        return model;
    }

    /**
     * 跳转到我的公告
     *
     * @return
     */
    @GetMapping("/notice/toMeNotice")
    public ModelAndView toMeNotice(ModelAndView model, Integer userId) {
        model.addObject("userId", userId);
        model.setViewName("webPage/meNotice");
        return model;
    }

    /**
     * 跳转到我的评论
     *
     * @return
     */
    @GetMapping("/comment/toMeComment")
    public ModelAndView toMeComment(ModelAndView model, Integer userId) {
        model.addObject("userId", userId);
        model.setViewName("webPage/meComment");
        return model;
    }

    /**
     * 我的出入记录
     *
     * @param model
     * @return
     */
    @RequestMapping("/webPage/toAccessPage")
    public ModelAndView toWebAccessPage(ModelAndView model, Integer userId) {
        model.addObject("userId", userId);
        model.setViewName("/webPage/userAccess");
        return model;
    }

    // | -------------------------------------------------------------------------------
    // |                             前台所有的页面跳转写这里 End
    // | -------------------------------------------------------------------------------


    // | -------------------------------------------------------------------------------
    // |                             后台所有的页面跳转写这里 Start
    // | -------------------------------------------------------------------------------

    /**
     * 跳转到项目中所有icon图标的查看
     *
     * @return
     */
    @RequestMapping("/findIcon")
    public String toIconPage() {
        return "error/unicode";
    }

    /**
     * 跳转到楼宇管理
     *
     * @param model
     * @return
     */
    @GetMapping("/building/toBuildingPage")
    public ModelAndView toBuildingPage(ModelAndView model) {
        model.setViewName("adminPage/unitBuilding/unitBuildingPage");
        return model;
    }

    /**
     * 跳转到楼宇信息修改
     *
     * @param model
     * @return
     */
    @GetMapping("/building/toEditWindow")
    public ModelAndView toEditWindow(ModelAndView model, Integer id) {
        // 查询要修改的信息
        UnitBuilding unitBuilding = unitBuildingService.selectByPrimaryKey(id);
        model.addObject("obj", unitBuilding);
        model.addObject("addUrl", "updateInfo");
        model.setViewName("adminPage/unitBuilding/editPage");
        return model;
    }

    /**
     * 跳转到楼宇信息新增
     *
     * @param model
     * @return
     */
    @GetMapping("/building/toAddWindow")
    public ModelAndView toAddWindow(ModelAndView model) {
        model.setViewName("adminPage/unitBuilding/addPage");
        model.addObject("addUrl", "insertInfo");
        return model;
    }

    /**
     * 跳转到住房管理
     *
     * @param model
     * @return
     */
    @GetMapping("/house/toHousePage")
    public ModelAndView toHousePage(ModelAndView model) {
        // 查询楼宇信息作为筛选条件
        List<UnitBuilding> buildingList = unitBuildingService.selectAllInfo(null);
        Integer maxCount = -1;
        // 循环查出最大单元数
        for (UnitBuilding unitBuilding : buildingList) {
            Integer unitCount = unitBuilding.getUnitCount();
            if (maxCount == -1) {
                maxCount = unitCount;
            } else if (maxCount < unitCount) {
                maxCount = unitCount;
            }
        }
        model.addObject("buildingList", buildingList);
        model.addObject("maxCount", maxCount);
        model.setViewName("adminPage/houseInfo/houseInfoPage");
        return model;
    }

    /**
     * 跳转到住房信息修改
     *
     * @param model
     * @return
     */
    @GetMapping("/house/toEditWindow")
    public ModelAndView toHouseEditWindow(ModelAndView model, Integer id) {
        // 查询要修改的数据
        HouseInfo houseInfo = houseInfoService.selectByPrimaryKey(id);
        // 查询楼宇信息
        List<UnitBuilding> buildingList = unitBuildingService.selectAllInfo(null);
        Integer maxUnit = -1;
        Integer maxFloor = -1;
        // 循环查出所属楼宇的单元数和楼层
        for (UnitBuilding unitBuilding : buildingList) {
            if (unitBuilding.getBuildingNum().toString().equals(houseInfo.getParentBuilding())) {
                // 保存最大单元数和最高楼层
                maxUnit = unitBuilding.getUnitCount();
                maxFloor = unitBuilding.getFloorCount();
            }
        }
        model.addObject("obj", houseInfo);
        model.addObject("buildingList", buildingList);
        model.addObject("maxUnit", maxUnit);
        model.addObject("maxFloor", maxFloor);
        model.addObject("addUrl", "updateInfo");
        model.setViewName("adminPage/houseInfo/editPage");
        return model;
    }

    /**
     * 跳转到住房出售页面
     *
     * @return
     */
    @GetMapping("/house/toSoldWindow")
    public ModelAndView toHouseSoldWindow(ModelAndView model, Integer id) {
        model.addObject("addUrl", "soldInfo");
        model.addObject("obj", houseInfoService.selectByPrimaryKey(id));
        model.addObject("userList", userService.selectDataByParam(null));
        model.setViewName("adminPage/houseInfo/soldPage");
        return model;
    }

    /**
     * 跳转到查看成员页面
     *
     * @return
     */
    @GetMapping("/userUnitRelation/toUserUnitRelationPage")
    public ModelAndView toUserUnitRelationPage(ModelAndView model, Integer id) {
        model.addObject("userList", userService.selectDataByParam(null));
        model.addObject("unitId", id);
        model.setViewName("adminPage/userUnitRelation/userUnitRelation");
        return model;
    }

    /**
     * 跳转到查看成员页面
     *
     * @return
     */
    @GetMapping("/userUnitRelation/toAddWindow")
    public ModelAndView toUserUnitRelationAdd(ModelAndView model, Integer id) {
        model.addObject("userList", userService.selectDataByParam(null));
        model.addObject("unitId", id);
        model.addObject("addUrl", "insertInfo");
        model.setViewName("adminPage/userUnitRelation/addPage");
        return model;
    }

    /**
     * 跳转到住房信息新增
     *
     * @param model
     * @return
     */
    @GetMapping("/house/toAddWindow")
    public ModelAndView toHouseAddWindow(ModelAndView model) {
        // 查询楼宇信息
        List<UnitBuilding> buildingList = unitBuildingService.selectAllInfo(null);
        Integer maxUnit = -1;
        Integer maxFloor = -1;
        // 循环查出所属楼宇的单元数和楼层
        for (UnitBuilding unitBuilding : buildingList) {
            Integer floorCount = unitBuilding.getFloorCount();
            Integer unitCount = unitBuilding.getUnitCount();
            if (unitCount > maxUnit) {
                maxUnit = unitCount;
            }
            if (floorCount > maxFloor) {
                maxFloor = floorCount;
            }
        }
        model.addObject("buildingList", buildingList);
        model.addObject("maxUnit", maxUnit);
        model.addObject("maxFloor", maxFloor);
        model.addObject("addUrl", "insertInfo");
        model.setViewName("adminPage/houseInfo/addPage");
        return model;
    }

    /**
     * 跳转到车位收费管理
     *
     * @param model
     * @return
     */
    @GetMapping("/carPark/toCarParkPage")
    public ModelAndView toCarParkPage(ModelAndView model) {
        model.setViewName("adminPage/carPark/carParkPage");
        return model;
    }

    /**
     * 跳转到车位管理
     *
     * @param model
     * @return
     */
    @GetMapping("/carPark/toCarParkMainPage")
    public ModelAndView toCarParkMainPage(ModelAndView model) {
        model.setViewName("adminPage/carPark/carParkMainPage");
        return model;
    }

    /**
     * 跳转到新增车位信息
     *
     * @param model
     * @return
     */
    @GetMapping("/carPark/toCarParkAddWindow")
    public ModelAndView toCarParkAddWindow(ModelAndView model) {
        model.addObject("addUrl", "insertInfo");
        model.setViewName("adminPage/carPark/addPage");
        return model;
    }

    /**
     * 跳转到修改车位信息
     *
     * @param model
     * @return
     */
    @GetMapping("/carPark/toCarParkEditWindow")
    public ModelAndView toCarParkEditWindow(ModelAndView model, Integer id, String type) {
        // 根据ID查询车位信息
        CarPark carPark = carParkService.selectByPrimaryKey(id);
        model.addObject("addUrl", "edit".equals(type) ? "updateInfo" : "charge/insertInfo");
        model.addObject("obj", carPark);
        model.addObject("type", type);
        model.addObject("readDate", DateUtil.format(new Date(), "yyyy年MM月dd号 HH:mm:ss"));
        model.addObject("userList", userService.selectDataByParam(null));
        model.setViewName("adminPage/carPark/editPage");
        return model;
    }

    /**
     * 跳转到收费项管理
     *
     * @param model
     * @return
     */
    @GetMapping("/propertyChargeItem/toPropertyChargeItemPage")
    public ModelAndView toPropertyChargeItemPage(ModelAndView model) {
        model.setViewName("adminPage/property/propertyChargeItem/propertyChargeItemPage");
        return model;
    }

    /**
     * 跳转到新增收费项列表
     *
     * @param model
     * @return
     */
    @GetMapping("/propertyChargeItem/toAddWindow")
    public ModelAndView toPropertyChargeItemAddWindow(ModelAndView model) {
        model.setViewName("adminPage/property/propertyChargeItem/addPage");
        model.addObject("addUrl", "insertInfo");
        return model;
    }

    /**
     * 跳转到收费记录管理
     *
     * @param model
     * @return
     */
    @GetMapping("/propertyChargeVisit/toPropertyChargeVisitPage")
    public ModelAndView toPropertyChargeVisitPage(ModelAndView model) {
        // 查询所有可用的缴费项信息
        List<PropertyChargeItem> items = propertyChargeItemService.selectDataAllInfo(null);
        model.setViewName("adminPage/property/propertyChargeVisit/propertyChargeVisitPage");
        model.addObject("itemList", items);
        return model;
    }

    /**
     * 跳转到收费记录管理
     *
     * @param model
     * @return
     */
    @GetMapping("/MyPropertyChargeVisit/toPropertyChargeVisitPage")
    public ModelAndView toPropertyChargeVisitPage2(ModelAndView model,@RequestParam String clientName) {
        // 查询所有可用的缴费项信息
        List<PropertyChargeItem> items = propertyChargeItemService.selectDataAllInfo(null);
        model.setViewName("webPage/MyPropertyCharge");
        model.addObject("itemList", items);
        model.addObject("clientName", clientName);
        return model;
    }

    /**
     * 跳转到录入收费记录
     *
     * @param model
     * @return
     */
    @GetMapping("/propertyChargeVisit/toAddWindow")
    public ModelAndView toPropertyChargeVisitAddPage(ModelAndView model) {
        // 查询所有可用的缴费项信息
        List<PropertyChargeItem> items = propertyChargeItemService.selectDataAllInfo(null);
        // 查询所有房间信息
        List<HouseInfo> houseInfos = houseInfoService.selectDataAllInfo(null);
        model.addObject("houseInfos", houseInfos);
        model.setViewName("adminPage/property/propertyChargeVisit/addPage");
        model.addObject("addUrl", "insertInfo");
        model.addObject("chargeItems", items);
        // 直接赋值抄表时间，时间为本月一号
        int month = DateUtil.month(new Date()) + 1;
        String nowDateStr = DateUtil.now();
        // 将月份替换，同时将日改成1号
        String[] split = nowDateStr.split("-");
        if (month + "".length() == 1) {
            split[1] = "0" + month;
        } else {
            split[1] = month + "";
        }
        split[2] = "01";
        String readDateStr = split[0] + "-" + split[1] + "-" + split[2] + " 00:00:00";
        System.out.println("转换后的抄表时间" + readDateStr);
        model.addObject("readDate", readDateStr);
        return model;
    }

    /**
     * 欠款缴纳页面
     *
     * @param model
     * @return
     */
    @GetMapping("/propertyPayVisit/toPayWindow")
    public ModelAndView toPayWindow(ModelAndView model, Integer id) {
        model.addObject("obj", propertyChargeVisitService.selectByPrimaryKey(id));
        model.setViewName("adminPage/property/propertyChargeVisit/payOrder");
        model.addObject("addUrl", "insertInfo");
        model.addObject("chargeId", id);
        return model;
    }

    /**
     * 跳转到修改收费记录
     *
     * @param model
     * @return
     */
    @GetMapping("/propertyChargeVisit/toEditWindow")
    public ModelAndView toPropertyChargeVisitUpdatePage(ModelAndView model, Integer id) {
        // 查询所有可用的缴费项信息
        List<PropertyChargeItem> items = propertyChargeItemService.selectDataAllInfo(null);
        // 查询所有房间信息
        List<HouseInfo> houseInfos = houseInfoService.selectDataAllInfo(null);
        // 查询要修改的对象
        PropertyChargeVisit chargeVisit = propertyChargeVisitService.selectByPrimaryKey(id);
        model.addObject("chargeItems", items);
        model.addObject("houseInfos", houseInfos);
        model.addObject("obj", chargeVisit);
        model.addObject("addUrl", "updateInfo");
        model.setViewName("adminPage/property/propertyChargeVisit/editPage");
        return model;
    }

    /**
     * 跳转到缴费记录管理
     *
     * @param model
     * @return
     */
    @GetMapping("/propertyPayVisit/toPropertyPayVisitPage")
    public ModelAndView toPropertyPayVisitPage(ModelAndView model, Integer userId, String clientName) {
        model.setViewName("adminPage/property/propertyPayVisit/propertyPayVisitPage");
        model.addObject("itemList", propertyChargeItemService.selectDataAllInfo(null));
        model.addObject("clientName", clientName);
        return model;
    }

    /**
     * 跳转到住户管理
     *
     * @param model
     * @return
     */
    @GetMapping("/user/toUserPage")
    public ModelAndView toUserPage(ModelAndView model) {
        model.setViewName("adminPage/user/user/userPage");
        return model;
    }

    /**
     * 跳转到新增住户信息
     *
     * @param model
     * @return
     */
    @GetMapping("/user/toUserAddWindow")
    public ModelAndView toAddUserPage(ModelAndView model) {
        model.addObject("addUrl", "insertInfo");
        model.setViewName("adminPage/user/user/addPage");
        return model;
    }

    /**
     * 跳转到住户管理
     *
     * @param model
     * @return
     */
    @GetMapping("/user/toUserEditWindow")
    public ModelAndView toUserEditWindow(ModelAndView model, Integer id) {
        // 查询要修改的用户信息
        User user = userService.selectByPrimaryKey(id);
        model.addObject("addUrl", "updateInfo");
        model.addObject("obj", user);
        model.setViewName("adminPage/user/user/editPage");
        return model;
    }

    /**
     * 跳转到住户投诉管理
     *
     * @param model
     * @return
     */
    @GetMapping("/userComplaint/toUserComplaintPage")
    public ModelAndView toUserComplaintPage(ModelAndView model) {
        model.setViewName("adminPage/user/userComplaint/userComplaintPage");
        return model;
    }

    /**
     * 跳转到住户投诉分配员工页面
     *
     * @param model
     * @return
     */
    @GetMapping("/userComplaint/toEditEmpPage")
    public ModelAndView toUserComplaintPage(ModelAndView model, Integer id, String type) {
        if ("repair".equals(type)) {
            model.addObject("addUrl", "../userRepair/updateInfoByObject");
        } else {
            model.addObject("addUrl", "updateInfoByObject");
        }
        model.addObject("id", id);
        model.addObject("empList", employeeService.selectDataByParam(null));
        model.setViewName("adminPage/user/userComplaint/editEmp");
        return model;
    }

    /**
     * 跳转到反馈页面
     *
     * @param model
     * @return
     */
    @GetMapping("/userComplaint/toEditResultPage")
    public ModelAndView toUserComplaintEditResultPage(ModelAndView model, Integer id, String type) {
        if ("repair".equals(type)) {
            model.addObject("addUrl", "../userRepair/updateInfoByObject");
        } else {
            model.addObject("addUrl", "updateInfoByObject");
        }
        model.addObject("id", id);
        model.setViewName("adminPage/user/userComplaint/editResult");
        return model;
    }

    /**
     * 跳转到反馈页面
     *
     * @param model
     * @return
     */
    @GetMapping("/userComplaint/toEditUserResultPage")
    public ModelAndView toEditUserResultPage(ModelAndView model, Integer id, String type) {
        if ("repair".equals(type)) {
            model.addObject("addUrl", "../userRepair/updateInfoByObject");
        } else {
            model.addObject("addUrl", "updateInfoByObject");
        }
        model.addObject("id", id);
        model.setViewName("adminPage/user/userComplaint/editUserResult");
        return model;
    }

    /**
     * 跳转到住户投诉分配员工页面
     *
     * @param model
     * @return
     */
    @GetMapping("/userComplaint/toFindPage")
    public ModelAndView toUserComplaintFindPage(ModelAndView model, Integer id, String type) {
        if ("repair".equals(type)) {
            model.addObject("obj", userRepairService.selectByPrimaryKey(id));
        } else {
            model.addObject("obj", userComplaintService.selectByPrimaryKey(id));
        }
        model.setViewName("adminPage/user/userComplaint/find");
        return model;
    }

    /**
     * 跳转到住户报修管理
     *
     * @param model
     * @return
     */
    @GetMapping("/userRepair/toUserRepairPage")
    public ModelAndView toUserRepairPage(ModelAndView model) {
        model.setViewName("adminPage/user/userRepair/userRepairPage");
        return model;
    }

    /**
     * 跳转到系统管理
     *
     * @param model
     * @return
     */
    @GetMapping("/adminInfo/toAdminPage")
    public ModelAndView toAdminPage(ModelAndView model) {
        model.setViewName("adminPage/admin/adminPage");
        return model;
    }

    /**
     * 跳转到新增管理员
     *
     * @param model
     * @return
     */
    @GetMapping("/adminInfo/toAddWindow")
    public ModelAndView toAdminAddWindow(ModelAndView model) {
        model.setViewName("adminPage/admin/addPage");
        model.addObject("addUrl", "insertInfo");
        return model;
    }

    /**
     * 跳转到修改管理员密码
     *
     * @param model
     * @return
     */
    @GetMapping("/adminInfo/toEditWindow")
    public ModelAndView toAdminEditWindow(ModelAndView model, Integer id) {
        model.addObject("obj", adminService.selectByPrimaryKey(id));
        model.addObject("addUrl", "updateInfo");
        model.setViewName("adminPage/admin/editPage");
        return model;
    }

    /**
     * 跳转到公告管理页面
     *
     * @param model
     * @return
     */
    @GetMapping("/notice/toDataPage")
    public ModelAndView toNoticeDataPage(ModelAndView model) {
        model.setViewName("adminPage/notice/noticePage");
        return model;
    }

    /**
     * 跳转到添加窗口
     *
     * @param model
     * @return
     */
    @GetMapping("/notice/toNoticeAddWindow")
    public ModelAndView toNoticeAddPage(ModelAndView model, String userName, Integer userId) {
        model.addObject("addUrl", "insertInfo");
        model.addObject("imgName", StrUtil.replace(UUID.randomUUID().toString(), "-", ""));
        model.addObject("userName", userName);
        model.addObject("userId", userId);
        model.setViewName("adminPage/notice/addPage");
        return model;
    }

    /**
     * 跳转到修改窗口
     *
     * @param model
     * @return
     */
    @GetMapping("/notice/toNoticeEditWindow")
    public ModelAndView toNoticeEditPage(ModelAndView model, Integer id) {
        Notice notice = noticeService.selectByPrimaryKey(id);
        model.addObject("addUrl", "updateInfo");
        model.addObject("imgName", StrUtil.replace(UUID.randomUUID().toString(), "-", ""));
        model.addObject("noticeInfo", notice);
        model.setViewName("adminPage/notice/editPage");
        return model;
    }

    /**
     * 跳转到查看窗口
     *
     * @param model
     * @return
     */
    @GetMapping("/notice/toFindPage")
    public ModelAndView toNoticeFindPage(ModelAndView model, Integer id, Integer userId) {
        Notice notice = noticeService.selectByPrimaryKey(id);
        // 查询该公告相关的用户评价
        List<Comment> comments = commentService.selectInfoByNoticeId(id);
        model.addObject("noticeInfo", notice);
        model.addObject("comments", comments);
        if (!StrUtil.isBlankOrUndefined(userId + "")) {
            model.addObject("userId", userId);
        }
        model.setViewName("adminPage/notice/findPage");
        return model;
    }

    /**
     * 跳转到评价页面
     *
     * @param model
     * @return
     */
    @GetMapping("/comment/toDataPage")
    public ModelAndView toCommentPage(ModelAndView model) {
        model.setViewName("adminPage/comment/commentPage");
        return model;
    }

    /**
     * 跳转到发布评价页面
     *
     * @param model
     * @return
     */
    @GetMapping("/comment/toAddPage")
    public ModelAndView toCommentAddPage(ModelAndView model, Integer userId, Integer noticeId) {
        // 保存信息
        model.addObject("userId", userId);
        model.addObject("noticeId", noticeId);
        model.addObject("addUrl", "insertInfo");
        model.setViewName("adminPage/comment/addPage");
        return model;
    }

    /**
     * 跳转到发布评价页面
     *
     * @param model
     * @return
     */
    @GetMapping("/comment/toEditPage")
    public ModelAndView toCommentEditPage(ModelAndView model, Integer id) {
        // 保存信息
        model.addObject("commentInfo", commentService.selectByPrimaryKey(id));
        model.addObject("addUrl", "updateInfo");
        model.setViewName("adminPage/comment/editPage");
        return model;
    }

    /**
     * 跳转到出入记录
     *
     * @param model
     * @return
     */
    @RequestMapping("/accessVisit/toDataPage")
    public ModelAndView toAccessPage(ModelAndView model) {
        model.setViewName("/adminPage/access/accessPage");
        return model;
    }

    /**
     * 跳转到员工管理
     *
     * @param model
     * @return
     */
    @RequestMapping("/employee/toDataPage")
    public ModelAndView toEmployeePage(ModelAndView model) {
        model.setViewName("/adminPage/employee/employeePage");
        return model;
    }

    /**
     * 跳转到员工管理
     *
     * @param model
     * @return
     */
    @RequestMapping("/employee/toAddPage")
    public ModelAndView toEmployeeAddPage(ModelAndView model) {
        model.addObject("addUrl", "insertInfo");
        model.setViewName("/adminPage/employee/addPage");
        return model;
    }

    /**
     * 跳转到员工管理
     *
     * @param model
     * @return
     */
    @RequestMapping("/employee/toEditPage")
    public ModelAndView toEmployeeEditPage(ModelAndView model, Integer id) {
        model.addObject("obj", employeeService.selectByPrimaryKey(id));
        model.addObject("addUrl", "updateInfo");
        model.setViewName("/adminPage/employee/editPage");
        return model;
    }

    /**
     * 跳转到统计分析
     *
     * @param model
     * @return
     */
    @RequestMapping("/asset/statisticAnalysis")
    public ModelAndView toStatisticAnalysisPage(ModelAndView model, Integer id) {
//        model.addObject("obj", employeeService.selectByPrimaryKey(id));
//        model.addObject("addUrl", "updateInfo");
        model.setViewName("/adminPage/asset/asset");
        return model;
    }

    // | -------------------------------------------------------------------------------
    // |                             后台所有的页面跳转写这里 End
    // | -------------------------------------------------------------------------------

}
