package com.blog.controller.user

import com.aliyuncs.exceptions.ClientException
import com.blog.pojo.User
import com.blog.service.user.UserRegisterService
import com.blog.utils.MailUtils
import com.blog.utils.SmsUtil
import com.blog.vo.BaseResult
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

/**
 *  @author JeungNyeongJae
 *  @date 2019/11/7
 *
 */
@RestController
@RequestMapping
class UserRegisterController {

    @Autowired
    private lateinit var userRegisterService: UserRegisterService

    @Resource
    private lateinit var redisTemplate: RedisTemplate<String , String>

    /**
     * 注册时发送验证短信
     * @param mobile // 用户手机号
     * @return status code
     */
    @GetMapping("/sendSms")
    fun sendSms(mobile: String): ResponseEntity<BaseResult> {
        // 生成四位数验证码
        val random = RandomStringUtils.randomNumeric(4)
        println(random)

        // 存入redis
        redisTemplate.opsForValue().set(mobile, random, 1, TimeUnit.HOURS)

        // 发送短信
        return try {
            val sendSmsResponse = SmsUtil.sendSms(mobile, random)

            if ("OK".equals(sendSmsResponse.code, ignoreCase = true)) {
                ResponseEntity.ok(BaseResult(0, "发送成功"))
            } else {
                ResponseEntity.ok(BaseResult(1, sendSmsResponse.message))
            }
        } catch (e: ClientException) {
            e.printStackTrace()
            ResponseEntity.ok(BaseResult(1, "发送失败"))
        }
    }

    /**
     * 验证手机验证码
     * @param mobile 手机号
     * @param code 验证码
     * @return status code
     */
    @GetMapping("/verifySms")
    fun verifySms(mobile: String, code: String): ResponseEntity<BaseResult> {
        if (redisTemplate.keys("*").contains(mobile)) {
            if (redisTemplate.opsForValue().get(mobile) == code) {
                return ResponseEntity.ok(BaseResult(0, "OK"))
            }
        }
        return ResponseEntity.ok(BaseResult(1, "Wrong"))
    }

    /**
     * 注册时发送邮箱激活账号
     * @param user // 用户
     * @return state code
     */
    @PostMapping("/sendMail")
    fun sendMail(@RequestBody user: User): ResponseEntity<BaseResult> {

        // 移除手机验证码
        if (redisTemplate.keys("*").contains(user.userMobile)) {
            redisTemplate.delete(user.userMobile)
        }

        // 产生activeCode
        val activeCode = UUID.randomUUID().toString().replace("-", "")

        // 邮件信息
        val activeUrl = "http://localhost:10010/v1" +
                "/web-service/activeMail?mobile=" + user.userMobile + "&activeCode=" + activeCode
        val content = "<div>\n" +
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
                "        </div>"
        try {
            // 发送邮件
            MailUtils.sendMail(user.userEmail, "博客网账号激活", content)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 将邮件激活码 放到redis中
        redisTemplate.opsForValue().set(user.userMobile, activeCode, 1, TimeUnit.DAYS)

        // 保存用户信息
        userRegisterService.saveUser(user)

        // 返回信息
        return ResponseEntity.ok(BaseResult(0, "邮件发送成功"))
    }

    /**
     * 激活账号
     * @param mobile // 手机号
     * @param activeCode // UUID
     * @return StateCode
     */
    @GetMapping("/activeMail")
    fun activeMail(mobile: String, activeCode: String): ResponseEntity<BaseResult> {
        // 1 从redis中获取code
        val redisCodes: String
        if (redisTemplate.keys("*").contains(mobile)) {
            redisCodes = redisTemplate.opsForValue().get(mobile) as String
        } else {
            return ResponseEntity.ok(BaseResult(1, "邮件失效，请重新发送"))
        }

        // 2 比较
        if (redisCodes != activeCode) {
            return ResponseEntity.ok(BaseResult(1, "验证码错误，请重新发送"))
        }

        // 移除redis中的数据
        redisTemplate.delete(mobile)

        // 激活
        this.userRegisterService.upDateByMobile(mobile)

        // 返回信息
        return ResponseEntity.ok(BaseResult(0, "激活成功！"))
    }

    /**
     * 通过手机号查询用户
     * @param mobile // 手机号
     * @return User
     */
    @GetMapping("/query")
    fun queryUser(mobile: String): ResponseEntity<BaseResult> {
        // 通过手机号查询用户
        return if (this.userRegisterService.findByMobile(mobile) != null) {
            ResponseEntity.ok(BaseResult(1, "该手机号已被注册！"))
        } else ResponseEntity.ok(BaseResult(0, "OK"))
    }

    /**
     * 通过手机号和密码进行查询用户
     * @param mobile // 手机号
     * @param password // 密码
     * @return User
     */
    @GetMapping("/queryUser")
    fun queryUser(@RequestParam("mobile") mobile: String, @RequestParam("password") password: String): ResponseEntity<User> {
        // 通过手机号查询用户
        val user = this.userRegisterService.findByMobile(mobile)

        // 非空判断&密码校验
        return if (user == null || user.userPassword != password) {
            ResponseEntity.ok(null)
        } else ResponseEntity.ok(user)
    }
}
