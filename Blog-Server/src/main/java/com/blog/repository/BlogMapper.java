package com.blog.repository;

import com.blog.pojo.Blog;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author JeungNyeongJae
 * @date 2020/1/6
 */
@org.apache.ibatis.annotations.Mapper
public interface BlogMapper extends Mapper<Blog> {
}
