package com.roydon.domain.vo;

import lombok.Data;
import lombok.Getter;

/**
 * 用户更新 VO
 */
@Getter
@Data
public class UserUpdateVO {

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 性别
     */
    private Integer gender;

    public UserUpdateVO setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public UserUpdateVO setGender(Integer gender) {
        this.gender = gender;
        return this;
    }

    @Override
    public String toString() {
        return "UserUpdateVO{" +
                "nickname='" + nickname + '\'' +
                ", gender=" + gender +
                '}';
    }

}
