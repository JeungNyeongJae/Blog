package com.blog.service.blog

import com.blog.dao.BlogMapper
import com.blog.pojo.Blog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 *  @author JeungNyeongJae
 *  @date 2019/11/7
 *
 */
@Service
class BlogService {

    @Autowired
    private lateinit var blogMapper: BlogMapper

    /**
     * 查询所有
     * @return List
     */
    fun findAllBlog(id: Long?): List<Blog> {
        return blogMapper.selectAll()
    }
}
