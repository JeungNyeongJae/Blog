package com.blog.security.service.impl;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author JeungNyeongJae
 * @date 2019/12/3
 */

public class GrantedAuthorityImpl implements GrantedAuthority {

    private String authority;

    public GrantedAuthorityImpl(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() { return this.authority; }
}
