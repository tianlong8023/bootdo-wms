package com.bootdo.miniApp.domain;

import java.io.Serializable;
import java.util.Date;


/**
 * 小程序用户表
 *
 * @author sky.liu
 * @email 18798620563@163.com
 * @date 2018-10-14 14:48:29
 */
public class AppUserDO implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键ID
    private Integer id;
    //用户业务主键
    private String userBid;
    //用户唯一标识
    private String openid;
    //会话密钥
    private String sessionKey;
    //用户在开放平台的唯一标识符
    private String unionid;
    //是否删除：1-已删除，0-未删除
    private Integer isDeleted;
    //创建人id
    private Integer creatorId;
    //创建日期
    private Date gmtCreate;
    //更新人id
    private Integer modifierId;
    //更新日期
    private Date gmtModified;

    public AppUserDO() {
    }

    public AppUserDO(String userBid, String openid, String sessionKey, String unionid) {
        this.userBid = userBid;
        this.openid = openid;
        this.sessionKey = sessionKey;
        this.unionid = unionid;
    }

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
     * 设置：是否删除：1-已删除，0-未删除
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取：是否删除：1-已删除，0-未删除
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置：创建人id
     */
    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * 获取：创建人id
     */
    public Integer getCreatorId() {
        return creatorId;
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

    /**
     * 设置：更新人id
     */
    public void setModifierId(Integer modifierId) {
        this.modifierId = modifierId;
    }

    /**
     * 获取：更新人id
     */
    public Integer getModifierId() {
        return modifierId;
    }

    /**
     * 设置：更新日期
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * 获取：更新日期
     */
    public Date getGmtModified() {
        return gmtModified;
    }
}
