package com.blog.dao

import com.blog.pojo.User
import tk.mybatis.mapper.common.Mapper

/**
 *  @author JeungNyeongJae
 *  @date 2019/11/7
 *
 */
@org.apache.ibatis.annotations.Mapper
interface UserMapper : Mapper<User>
