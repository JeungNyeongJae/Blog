package com.blog.service.user;

import com.blog.pojo.User;
import com.blog.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 *  @author JeungNyeongJae
 *  @date 2019/11/7
 *
 */
@Service
public class UserRegisterService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 注册保存用户信息
     * @param user 用户
     */
    public void saveUser(User user) {
        user.setUserName("匿名用户");
        user.setStatus(0);
        userMapper.insert(user);
    }

    /**
     * 根据用户手机号激活
     * @param mobile 手机号
     */
    public void upDateByMobile(String mobile) {
        User user = this.findByMobile(mobile);
        if (user != null) {
            user.setStatus(1);
        }
        userMapper.updateByPrimaryKey(user);
    }

    /**
     * 根据手机查找用户
     * @param mobile 用户手机号
     * @return User
     */
    public User findByMobile(String mobile) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userMobile", mobile);
        return this.userMapper.selectOneByExample(example);
    }
}
