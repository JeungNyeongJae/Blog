package com.blog.controller.blog

import com.blog.pojo.Blog
import com.blog.service.blog.BlogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *  @author JeungNyeongJae
 *  @date 2019/11/7
 *
 */
@RestController
@RequestMapping
class BlogController {

    @Autowired
    private lateinit var blogService: BlogService

    fun findAll(id: Long?): List<Blog> {
        return blogService.findAllBlog(id)
    }
}
