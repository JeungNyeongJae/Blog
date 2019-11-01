package com.blog.controller;

import com.alibaba.druid.Constants;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.blog.pojo.User;
import com.blog.service.UserService;
import com.blog.utils.MailUtils;
import com.blog.utils.SmsUtil;
import com.blog.vo.BaseResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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
    private StringRedisTemplate redisTemplate;

    @Resource
    private HttpSession httpSession;

    @Resource
    private RestTemplate restTemplate;



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
     * 注册时发送邮箱激活账号
     * @param user // 用户
     * @return
     */
    public ResponseEntity<Void> regist(String checkcode, User user){
        //校验验证码
        String code = httpSession.getAttribute(user.getUserMobile()).toString();
        if(code!=null&&!"".equals(code.trim())){
            if(code.equals(checkcode)){
                //移除激活码
                httpSession.removeAttribute(user.getUserMobile());
                //调用服务
                String url = "http://localhost:8088";   //假的链接
                // 第一个参数：url
                // 第二个参数：数据
                // 第三个参数：返回值类型
                ResponseEntity<String>entity =restTemplate.postForEntity(url,user,String.class);
                /******************发送邮件******************/
                /**
                 * 1 产生activeCode
                 * 2 将activeCode保存到redis中
                 *  注意“地址栏的参数中不能带-
                 * 3 拼接url <a href="localhost:8092/regist/activeMail?telephone=xxx&activeCode=xxxx-sss-aaa">速运快递账号激活</a>
                 */
                String activeCode = UUID.randomUUID().toString().replace("-","");
                // 将code 放到redis中
                // 第三个参数：时间
                // 第四个参数：单位
                redisTemplate.opsForValue().set(user.getUserMobile(), activeCode,1,TimeUnit.DAYS);
                //发送邮件
                String activeUrl = "http://localhost:8088" +  //链接假的
                        "/regist/activeMail?telephone="+user.getUserMobile()+"&activeCode="+activeCode;
                String content = "<a href='"+activeUrl+"'>百合网账号激活链接</a>";
                try {
                    MailUtils.sendMail(user.getUserEmail(),"博客网账号激活",content);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                HttpStatus statusCode = entity.getStatusCode();
                return new ResponseEntity<>(statusCode);

            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
