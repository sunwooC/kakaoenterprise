package com.kakaoenterprise.web.dto;

import com.kakaoenterprise.domain.user.User;

import lombok.Data;

/**
 * Local 가입시 요청하는 Dto
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@Data
public class AuthJoinReqDto {
	private String username;
	private String nickname;
	private String password;
	private String email;
	private String agerange;

	public User toEntity() {
		return User.builder().username(username).nickname(nickname).password(password).agerange(agerange).email(email)
				.build();
	}
}
