package com.blog.security.exception;

/**
 * @author JeungNyeongJae
 * @date 2019/12/3
 */
public class TokenException extends BaseException {

    private static final long serialVersionUID = 1L;

    public TokenException(String message) {
        super(message);
    }
}
