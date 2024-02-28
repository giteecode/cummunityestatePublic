package com.project.property.config;

import cn.hutool.extra.mail.MailAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MailConfig {

    @Value("${my.mail.host}")
    private String host;

    @Value("${my.mail.username}")
    private String userName;

    @Value("${my.mail.password}")
    private String password;

    @Value("${my.mail.port}")
    private Integer port;

    @Value("${my.mail.from}")
    private String from;

    @Value("${my.mail.defaultEncoding}")
    private String defaultEncoding;

    @Value("${my.mail.auth}")
    private boolean auth;

    @Value("${my.mail.timeout}")
    private long timeout;

    @Value("${my.mail.socketFactoryClass}")
    private String socketFactoryClass;

    @Value("${my.mail.socketFactoryPort}")
    private Integer socketFactoryPort;

    @Value("${my.mail.sslEnable}")
    private boolean sslEnable;

    @Bean
    public MailAccount createMailAccount() {
        MailAccount account = new MailAccount();
        account.setHost(host);
        account.setPort(port);
        account.setAuth(auth);
        account.setFrom(from);
        account.setUser(userName);//这里名字需要是发送账户名字
        account.setPass(password); //密码，授权码
        account.setSocketFactoryClass(socketFactoryClass);
        account.setSocketFactoryPort(socketFactoryPort);
        account.setTimeout(timeout);
        account.setSslEnable(sslEnable);
        return account;
    }

}
