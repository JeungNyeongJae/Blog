package com.blog.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.blog.pojo.User;
import com.blog.service.UserService;
import com.blog.utils.SmsUtil;
import com.blog.vo.BaseResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author JeungNyeongJae
 */

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private StringRedisTemplate redisTemplate;

    /**
     * 注册保存用户信息
     * @param user // 用户
     * @return
     */
    @PostMapping("/register")
    private ResponseEntity<Object> rigister(@RequestBody User user){

        this.userService.saveUser(user);

        return ResponseEntity.ok( new BaseResult(0 , "注册成功"));
    }

    /**
     * 注册时发送验证短信
     * @param user // 用户
     * @return
     */
    @PostMapping("/sms")
    private ResponseEntity<BaseResult> sendSms(@RequestBody User user){

        // 生成四位数验证码
        String random = RandomStringUtils.randomNumeric(4);
        System.out.println(random);

        // 存入redis
        redisTemplate.opsForValue().set( user.getUserMobile() , random , 1 , TimeUnit.HOURS);

        // 发送短信
        try {
            SendSmsResponse sendSmsResponse = SmsUtil.sendSms(user.getUserMobile(), random);

            if ( "OK".equalsIgnoreCase( sendSmsResponse.getCode() )){
                return ResponseEntity.ok(new BaseResult( 0 , "发送成功"));
            } else {
                return ResponseEntity.ok(new BaseResult(1, sendSmsResponse.getMessage()));
            }
        } catch (ClientException e) {
            e.printStackTrace();
            return ResponseEntity.ok(new BaseResult( 1 , "发送失败"));
        }
    }

    /**
     * 通过手机号和密码进行查询
     * @param mobile // 手机号
     * @param password // 密码
     * @return StateCode
     */
    @GetMapping("/query")
    public ResponseEntity<User> queryUser(@RequestParam("mobile") String mobile , @RequestParam("password") String password){

        // 通过手机号查询用户
        User user = this.userService.findByMobile( mobile );

        // 非空判断&密码校验
        if(user == null || !user.getUserPassword().equals(password)){

            return ResponseEntity.ok( null );
        }

        return ResponseEntity.ok( user );
    }
}
