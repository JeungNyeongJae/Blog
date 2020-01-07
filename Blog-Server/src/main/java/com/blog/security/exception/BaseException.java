package com.blog.security.exception;

/**
 * @author JeungNyeongJae
 * @date 2019/12/3
 */
public class BaseException extends RuntimeException {

    BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
