package com.blog.pojo;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import java.io.Serializable;

/**
 * @author JeungNyeongJae
 */

@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @NotNull
    private Long userId;

    @Column(name="user_name")
    private String userName;

    @Column(name="user_password")
    @NotNull
    private String userPassword;

    @Column(name="user_face")
    private String userFace;

    @Column(name="user_email")
    @NotNull
    private String userEmail;

    @Column(name="user_mobile")
    @NotNull
    private String userMobile;

    @Column(name="status")
    @NotNull
    private Integer status;
}
