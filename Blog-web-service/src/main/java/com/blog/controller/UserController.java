package com.blog.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.blog.pojo.User;
import com.blog.service.UserService;
import com.blog.utils.MailUtils;
import com.blog.utils.SmsUtil;
import com.blog.vo.BaseResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Yinchao
 */

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 注册时发送验证短信
     * @param mobile // 用户手机号
     * @return state code
     */
    @PostMapping("/sms")
    private ResponseEntity<BaseResult> sendSms(@RequestParam("mobile") String mobile){

        // 生成四位数验证码
        String random = RandomStringUtils.randomNumeric(4);
        System.out.println(random);

        // 存入redis
        redisTemplate.opsForValue().set( mobile , random , 1 , TimeUnit.HOURS);

        // 发送短信
        try {
            SendSmsResponse sendSmsResponse = SmsUtil.sendSms(mobile, random);

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
     * 注册时发送邮箱激活账号
     * @param user // 用户
     * @return state code
     */
    @PostMapping("/sendMail")
    public ResponseEntity<BaseResult> sendMail(User user){
        //移除激活码
        redisTemplate.delete(user.getUserMobile());

        // 1 产生activeCode
        String activeCode = UUID.randomUUID().toString().replace("-","");
        // 2 将code 放到redis中
        redisTemplate.opsForValue().set(user.getUserMobile(), activeCode,1,TimeUnit.DAYS);
        redisTemplate.opsForValue().set("user" , user , 1 , TimeUnit.DAYS);
        // 3 发送邮件
        String activeUrl = "http://localhost:10010/v1" +
                "/web-service/activeMail?telephone="+user.getUserMobile()+"&activeCode="+activeCode;
        String content = "您好，\n\t" +
                "欢迎来到JeungNyeongJae博客网。为了您的帐户安全，<a href=\'"+activeUrl+ "\'>请验证您的邮箱</a>。\n\t" +
                "现在您可以获得各种优质服务，包括：\n\n\t" +
                "· 订阅关注喜爱博主\n\t" +
                "· 发表个人博文\n\t" +
                "· 获得他人关注，喜爱，打赏\n\t" +
                "· 在线客服问答\n\n\t" +
                "更改密码或更新账号，请访问<a href=\'" + activeUrl + "\'>账户管理网站</a>。\n\t" +
                "祝您好运！";
        try {
            MailUtils.sendMail(user.getUserEmail(),"博客网账号激活",content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(new BaseResult(0 , "邮件发送成功"));
    }

    /**
     * 通过手机号和密码进行查询
     * @param mobile // 手机号
     * @param activeCode
     * @return StateCode
     */
    @PostMapping("/activeMail")
    public ResponseEntity<Void> activeMail(String mobile,String activeCode){
        // 1 从redis中获取code
        String redisCodes =redisTemplate.opsForValue().get(mobile).toString();
        // 2 比较
        if(redisCodes==null | !redisCodes.equals(activeCode)){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // 移除redis中的数据
        redisTemplate.delete(mobile);

        // 激活
        this.userService.saveUser((User)redisTemplate.opsForValue().get("user"));

        redisTemplate.delete("user");

        return new ResponseEntity<>(HttpStatus.OK);
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
