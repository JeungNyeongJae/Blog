package com.blog.service.blog;

import com.blog.repository.BlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author JeungNyeongJae
 * @date 2020/1/6
 */
@Service
public class BlogService {

    @Autowired
    private BlogMapper blogMapper;

}
