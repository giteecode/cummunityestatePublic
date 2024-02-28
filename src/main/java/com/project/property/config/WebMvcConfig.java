package com.project.property.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc相关配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }

    /**
     * 设置首页的跳转
     * 将该方法返回的WebMvcConfigurer添加进Spring容器
     *
     * @return
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        WebMvcConfigurer webMvcConfigurer = new WebMvcConfigurer() {

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // 配置不拦截静态资源
                registry.addResourceHandler("/**").addResourceLocations("classpath:/static/", "file:./resource.file/");
                // 配置Swagger页面
                registry.addResourceHandler("/swagger-ui.html")
                        .addResourceLocations("classpath:/META-INF/resources/");
                registry.addResourceHandler("/doc.html")
                        .addResourceLocations("classpath:/META-INF/resources/");
                // 配置webjars为静态资源
                registry.addResourceHandler("/webjars/**")
                        .addResourceLocations("classpath:/META-INF/resources/webjars/");

            }

            /**
             * 扩展添加视图映射
             * @param registry
             */
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
//                registry.addViewController("/").setViewName("webPage/index");     //拦截 /
                registry.addViewController("/main.html").setViewName("login");   //拦截 main.html
                registry.addViewController("/index").setViewName("webPage/index");   //拦截 main.html
                //设置一个视图解析器，登陆成功后跳转到 main.html，而main.html跳转的则是登陆成功的页面，这样可以防止表单重复提交
                registry.addViewController("/main.html").setViewName("system/main");
            }

            /**
             * 添加拦截器
             * @param registry
             */
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new LoginInterceptor())
                        .addPathPatterns("/**")
                        .excludePathPatterns("/", "/admin", "/webjars/**", "/static/**", "/adminStatic/**", "/webStatic/**", "/webPage/**", "/index", "/userComplaint/insertInfo",
                                "/userRepair/insertInfo", "/css/**", "/js/**", "/lib/**", "/images/**", "/fonts/**", "/upload/**", "/doc.html/**", "/error-404", "/error-400", "/error-500",
                                "/favicon.ico", "/system/toMainPage", "/system/toLoginPage", "/system/loginOut", "/visit/**", "/system/toTimeOutPage", "/system/timeOut"
                                , "/swagger-ui.html", "/swagger-ui.html/**", "/swagger-resources/**", "/findIcon", "/user/updateInfo", "/user/updateInfo", "/accessVisit/insertInfo"
                                , "/carPark/getDataByPage", "/house/getDataByPage", "/house/toWebBuyPage", "/carPark/toWebBuyPage", "/userRepair/updateInfoByObject"
                                , "/carPark/toWebCarParkPage", "/house/toWebHousePage", "/carPark/updateInfo", "/house/soldInfo", "/comment/**"
                                , "/userComplaint/getDataByPageWeb", "/userComplaint/toFindPage", "/userComplaint/toEditUserResultPage", "/userComplaint/updateInfoByObject",
                                "/userRepair/getDataByPageWeb", "/accessVisit/getDataByPage", "/user/toUserEditWindow", "/notice/**", "/system/uploadImages"
                                , "/propertyPayVisit/toPropertyPayVisitPage", "/MyPropertyChargeVisit/toPropertyChargeVisitPage", "/propertyPayVisit/getDataByPage", "/propertyChargeVisit/getDataByPage"
                                , "/api/file/upload/*", "/carPark/charge/getDataByPage");
            }
        };
        return webMvcConfigurer;
    }

}