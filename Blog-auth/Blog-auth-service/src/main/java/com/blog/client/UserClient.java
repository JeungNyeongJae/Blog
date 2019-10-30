package com.blog.client;

import com.blog.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author JeungNyeongJae
 */

@FeignClient("web-service")
public interface UserClient {

    /**
     * 获取电话密码
     * @param mobile
     * @param password
     * @return
     */
    @GetMapping("query")
    ResponseEntity<User> queryUser(@RequestParam("mobile") String mobile,
                                   @RequestParam("password") String password);
}
