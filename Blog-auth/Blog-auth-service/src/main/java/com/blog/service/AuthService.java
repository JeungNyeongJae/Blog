package com.blog.service;

import com.blog.client.UserClient;
import com.blog.config.JwtProperties;
import com.blog.entity.UserInfo;
import com.blog.pojo.User;
import com.blog.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author JeungNyeongJae
 */

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    /**
     * 登录 ， 生成 token
     * @param mobile
     * @param password
     * @return
     */
    public String login( String mobile , String password ) {

        try {
            // 远程调用查询数据
            ResponseEntity<User> resp = this.userClient.queryUser(mobile, password);
            if (!resp.hasBody()) {
                return null;
            }
            // 获取User
            User user = resp.getBody();

            // 生成token
            return JwtUtils.generateToken(new UserInfo( user.getUserId() , user.getUserName()),
                    jwtProperties.getPrivateKey(), jwtProperties.getExpire());
        } catch (Exception e){
            logger.error("生成token失败，用户名：{}", mobile, e);
        }
        return null;
    }
}
