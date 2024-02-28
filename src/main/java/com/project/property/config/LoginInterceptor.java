package com.project.property.config;

import com.alibaba.fastjson.JSON;
import com.project.property.entity.Admin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @comment: 登陆拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取session中存储的登录用户的信息
        Admin admin = (Admin) request.getSession().getAttribute("loginAdmin");

        Object parentMenu = request.getSession().getAttribute("parentMenu");
        Object subMenu = request.getSession().getAttribute("subMenu");
        if(admin == null || admin.getUserName() == null || parentMenu == null || subMenu == null){
            //判断是否是Ajax请求  获取到请求头中的Ajax参数
            String XRequested =request.getHeader("X-Requested-With");
            //Ajax请求中带有的参数
            String ajaxReq = "XMLHttpRequest";
            if(ajaxReq.equals(XRequested)) {
                Map<String, Object> map = new HashMap<>(1);
                map.put("result", "IsAdminAjax");
                String data = JSON.toJSONString(map);
                response.getWriter().write(data);
                return false;
            } else {
                System.out.println("拦截请求");
                System.out.println("请求地址为：" + request.getRequestURI());
                response.sendRedirect(request.getContextPath()+"/system/toTimeOutPage");
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
