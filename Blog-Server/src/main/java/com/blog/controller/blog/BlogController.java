package com.blog.controller.blog;

import com.blog.service.blog.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeungNyeongJae
 * @date 2020/1/6
 */
@RestController
@RequestMapping
public class BlogController {

    @Autowired
    private BlogService blogService;
}
