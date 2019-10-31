package com.blog.controller;

import com.blog.config.JwtProperties;
import com.blog.entity.UserInfo;
import com.blog.pojo.User;
import com.blog.service.AuthService;
import com.blog.utils.JwtUtils;
import com.blog.vo.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author JeungNyeongJae
 */


@RestController
@RequestMapping
public class AuthController {

    @Resource
    private AuthService authService;

    @Autowired
    private JwtProperties prop;
    @Autowired
    private HttpServletRequest req;

    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<BaseResult> login(@RequestBody User user){

        // 调用Service ， 获取token
        String token = this.authService.login( user.getUserMobile() , user.getUserPassword() );

        // 有token 登录
        if (StringUtils.isNotBlank( token )) {
            return ResponseEntity.ok(new BaseResult( 0 , "登录成功").append("token" , token));
        } else {

            // 没有token 失败
            return ResponseEntity.ok(new BaseResult( 1 , "登录失败"));
        }
    }

    /**
     * 验证用户信息
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser() {
        try {
            String token = req.getHeader("Authorization");
            // 获取token信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            // 成功后直接返回
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            // 抛出异常，证明token无效，直接返回401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
