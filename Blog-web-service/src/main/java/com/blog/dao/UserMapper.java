package com.blog.dao;

import com.blog.pojo.User;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author JeungNyeongJae
 */

@org.apache.ibatis.annotations.Mapper
public interface UserMapper extends Mapper<User> {
}
