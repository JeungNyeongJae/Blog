package com.blog.service.user;

import com.blog.pojo.User;
import com.blog.repository.UserMapper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @author JeungNyeongJae
 * @date 2019/12/2
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public User findByUsername(String mobile) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("user_mobile", mobile);
        userMapper.selectOneByExample(example);
        return userMapper.selectOneByExample(example);
    }
}
