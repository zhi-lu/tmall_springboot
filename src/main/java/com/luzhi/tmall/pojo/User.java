package com.luzhi.tmall.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * @author apple
 * @version jdk
 * // TODO : 2021/3/21
 * 关于用户的pojo层,创建
 */
@Entity
@Table(name = "user")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@SuppressWarnings("unused")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Transient
    private String anonymousName;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * @see #getAnonymousName()
     * 设置相关的匿名方法
     */
    public String getAnonymousName() {

        // 用户为两个字符的,(magic value)
        final int number = 2;
        // 判断用户名是否为空
        if (null == name) {
            anonymousName = null;
            return null;
        }
        // 判断相关的"匿名"不为空.
        if (name.length() <= 1) {
            return "*";
        }
        if (name.length() == number) {
            return name.charAt(0) + "*";
        } else {
            char[] chars = name.toCharArray();
            for (int i = 1; i < chars.length - 1; i++) {
                chars[i] = '*';
            }
            return new String(chars);
        }
    }

    public void setAnonymousName(String anonymousName) {
        this.anonymousName = anonymousName;
    }
}
