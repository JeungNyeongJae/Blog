package com.blog.service.user

import com.blog.dao.UserMapper
import com.blog.pojo.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.entity.Example

/**
 *  @author JeungNyeongJae
 *  @date 2019/11/7
 *
 */
@Service
class UserRegisterService {

    @Autowired
    private lateinit var userMapper: UserMapper

    /**
     * 注册保存用户信息
     * @param user 用户
     */
    fun saveUser(user: User) {
        user.userName = "匿名用户"
        user.status = 0
        userMapper.insert(user)
    }

    /**
     * 根据用户手机号激活
     * @param mobile 手机号
     */
    fun upDateByMobile(mobile: String) {
        val user = this.findByMobile(mobile)
        if (user != null) {
            user.status = 1
        }
        userMapper.updateByPrimaryKey(user)
    }

    /**
     * 根据手机查找用户
     * @param mobile 用户手机号
     * @return User
     */
    fun findByMobile(mobile: String): User? {
        val example = Example(User::class.java)
        val criteria = example.createCriteria()
        criteria.andEqualTo("userMobile", mobile)
        return this.userMapper.selectOneByExample(example)
    }
}
