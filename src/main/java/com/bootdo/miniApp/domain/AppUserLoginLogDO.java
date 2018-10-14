package com.bootdo.miniApp.domain;

import java.io.Serializable;
import java.util.Date;


/**
 * 小程序用户登录日志表
 *
 * @author sky.liu
 * @email 18798620563@163.com
 * @date 2018-10-14 14:48:38
 */
public class AppUserLoginLogDO implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键ID
    private Integer id;
    //用户登錄code
    private String loginCode;
    //用户业务主键
    private String userBid;
    //用户唯一标识
    private String openid;
    //会话密钥
    private String sessionKey;
    //用户在开放平台的唯一标识符
    private String unionid;
    //创建日期
    private Date gmtCreate;

    /**
     * 设置：主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：主键ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置：用户登錄code
     */
    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    /**
     * 获取：用户登錄code
     */
    public String getLoginCode() {
        return loginCode;
    }

    /**
     * 设置：用户业务主键
     */
    public void setUserBid(String userBid) {
        this.userBid = userBid;
    }

    /**
     * 获取：用户业务主键
     */
    public String getUserBid() {
        return userBid;
    }

    /**
     * 设置：用户唯一标识
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * 获取：用户唯一标识
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * 设置：会话密钥
     */
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    /**
     * 获取：会话密钥
     */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
     * 设置：用户在开放平台的唯一标识符
     */
    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    /**
     * 获取：用户在开放平台的唯一标识符
     */
    public String getUnionid() {
        return unionid;
    }

    /**
     * 设置：创建日期
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取：创建日期
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }
}
