package com.moyou.activity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Neason
 * @since 2019-08-23
 */
public class MoyTravelUser extends Model<MoyTravelUser> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "user_id", type = IdType.INPUT)
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户等级
     */
    private LocalDateTime userLevel;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 最后登陆ip
     */
    private String ipCard;

    /**
     * 登陆方式(pc,手机,二维码等等 预留)
     */
    private String loginType;

    /**
     * 头部图像地址
     */
    private String userHeadImage;

    /**
     * 描述
     */
    private String description;

    /**
     * 注册时间
     */
    private LocalDateTime registerDate;

    /**
     * 最后登陆时间
     */
    private LocalDateTime lastLoginDate;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public LocalDateTime getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(LocalDateTime userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getIpCard() {
        return ipCard;
    }

    public void setIpCard(String ipCard) {
        this.ipCard = ipCard;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getUserHeadImage() {
        return userHeadImage;
    }

    public void setUserHeadImage(String userHeadImage) {
        this.userHeadImage = userHeadImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

    @Override
    public String toString() {
        return "MoyTravelUser{" +
        "userId=" + userId +
        ", userName=" + userName +
        ", userPassword=" + userPassword +
        ", userLevel=" + userLevel +
        ", userPhone=" + userPhone +
        ", ipCard=" + ipCard +
        ", loginType=" + loginType +
        ", userHeadImage=" + userHeadImage +
        ", description=" + description +
        ", registerDate=" + registerDate +
        ", lastLoginDate=" + lastLoginDate +
        "}";
    }
}
