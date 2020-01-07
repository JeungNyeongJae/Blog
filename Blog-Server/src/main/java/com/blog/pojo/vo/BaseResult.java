package com.blog.pojo.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JeungNyeongJae
 */

@Data
public class BaseResult {

    private Map<String, Object> data = new HashMap<>();

    public BaseResult(Integer errno, String errmsg) {
        data.put("errno" , errno);
        data.put("errmsg" , errmsg);
    }

    public BaseResult append(String key , Object msg){
        data.put(key , msg);
        return this;
    }
}
