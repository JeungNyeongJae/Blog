package com.blog.service;

import com.blog.dao.UserMapper;
import com.blog.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @author JeungNyeongJae
 */

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 注册保存用户信息
     * @param user // 用户
     */
    public void saveUser(User user){
        userMapper.insert(user);
    }

    public User findByMobile(String mobile) {

        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("user_mobile" , mobile);
        return this.userMapper.selectOneByExample( example );
    }
}
