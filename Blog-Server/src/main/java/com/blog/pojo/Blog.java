package com.blog.pojo;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author JeungNyeongJae
 * @date 2019/11/7
 */
@Table(name = "blog")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Blog {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @NotNull
    private Long blogId;
}
