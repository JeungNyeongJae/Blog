package com.blog.controller.user;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.blog.pojo.User;
import com.blog.service.user.UserRegisterService;
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
public class UserRegisterController {

    @Autowired
    private UserRegisterService userRegisterService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 注册时发送验证短信
     * @param mobile // 用户手机号
     * @return status code
     */
    @GetMapping("/sendSms")
    public ResponseEntity<BaseResult> sendSms(String mobile){
        // 生成四位数验证码
        String random = RandomStringUtils.randomNumeric(4);

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
     * 验证手机验证码
     * @param mobile 手机号
     * @param code 验证码
     * @return status code
     */
    @GetMapping("/verifySms")
    public ResponseEntity<BaseResult> verifySms(String mobile , String code){
        if (redisTemplate.opsForValue().get(mobile).equals(code)) {
            return ResponseEntity.ok(new BaseResult( 0 , "OK"));
        }
        return ResponseEntity.ok(new BaseResult( 1 , "Wrong"));
    }

    /**
     * 注册时发送邮箱激活账号
     * @param user // 用户
     * @return state code
     */
    @PostMapping("/sendMail")
    public ResponseEntity<BaseResult> sendMail(@RequestBody User user){

        // 移除手机验证码
        redisTemplate.delete(user.getUserMobile());

        // 产生activeCode
        String activeCode = UUID.randomUUID().toString().replace("-","");

        // 邮件信息
        String activeUrl = "http://localhost:10010/v1" +
                "/web-service/activeMail?mobile="+user.getUserMobile()+"&activeCode="+activeCode;
        String content = "<div>\n" +
                "            <br>\n" +
                "            <h2>JeungNyeongJae</h2>\n" +
                "            <font color=\"#a9a9a9\">____________________________</font>\n" +
                "            <h4>您好: </h4>\n" +
                "            <h4>欢迎来到JeungNyeongJae博客网。为了您的帐户安全，请验证您的邮箱：</h4>\n" +
                "            <a href= \'" + activeUrl + "\'><button style=\"background-color: #2245ff; height: 40px; width: 180px; border-radius: 10px; \"><font style=\"color: aliceblue;\">激活</font></button></a>\n" +
                "            <br>\n" +
                "            <br>\n" +
                "            <div style=\"margin: 1px; border: 2px solid gray; border-radius: 5px\">\n" +
                "                <div style=\"margin-left: .75rem;\">\n" +
                "                    <h5>现在您可以获得各种优质服务，包括：</h5>\n" +
                "                    <h5>· 订阅关注喜爱博主</h5>\n" +
                "                    <h5>· 发表个人博文</h5>\n" +
                "                    <h5>· 获得他人关注，喜爱，打赏</h5>\n" +
                "                    <h5>· 在线客服问答</h5>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <br>\n" +
                "            <h4>更改密码或更新账号，请访问<a href= \"http://yunyeohyang.cn\">账户管理网站</a>。</h4>\n" +
                "            <h4>祝您好运！</h4>\n" +
                "            <font color=\"#a9a9a9\">____________________________</font>\n" +
                "            <br>\n" +
                "            <font style=\"color: #55595f; font-size: 14px\">本邮件由系统自动发送，请勿直接回复，如有任何意见或建议，请您通过<a href=\"http://www.yunyeohyang.cn\">这些方式</a>联系我们</font>\n" +
                "            <br>\n" +
                "            <font style=\"color: #55595f; font-size: 14px\">YunYeoHyang.cn</font>\n" +
                "        </div>";
        try {
            // 发送邮件
            MailUtils.sendMail(user.getUserEmail(),"博客网账号激活",content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 将邮件激活码 放到redis中
        redisTemplate.opsForValue().set(user.getUserMobile(), activeCode,1,TimeUnit.DAYS);

        // 保存用户信息
        userRegisterService.saveUser(user);

        // 返回信息
        return ResponseEntity.ok(new BaseResult(0 , "邮件发送成功"));
    }

    /**
     * 激活账号
     * @param mobile // 手机号
     * @param activeCode // UUID
     * @return StateCode
     */
    @GetMapping("/activeMail")
    public ResponseEntity<Void> activeMail(String mobile,String activeCode){
        // 1 从redis中获取code
        String redisCodes = (String) redisTemplate.opsForValue().get(mobile);

        // 2 比较
        if (redisCodes == null | (redisCodes != null && !redisCodes.equals(activeCode))) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 移除redis中的数据
        redisTemplate.delete(mobile);

        // 激活
        this.userRegisterService.upDateByMobile( mobile );

        // 返回信息
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 通过手机号查询用户
     * @param mobile // 手机号
     * @return User
     */
    @GetMapping("/query")
    public ResponseEntity<BaseResult> queryUser(String mobile){
        // 通过手机号查询用户
        if (this.userRegisterService.findByMobile( mobile ) != null) {
            return ResponseEntity.ok(new BaseResult( 1 , "该手机号已被注册！"));
        }
        return ResponseEntity.ok(new BaseResult( 0 , "OK"));
    }

    /**
     * 通过手机号和密码进行查询用户
     * @param mobile // 手机号
     * @param password // 密码
     * @return User
     */
    @GetMapping("/queryUser")
    public ResponseEntity<User> queryUser(@RequestParam("mobile") String mobile , @RequestParam("password") String password){
        // 通过手机号查询用户
        User user = this.userRegisterService.findByMobile( mobile );

        // 非空判断&密码校验
        if(user == null || !user.getUserPassword().equals(password)){
            return ResponseEntity.ok( null );
        }
        return ResponseEntity.ok( user );
    }
}
