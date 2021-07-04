package com.kakaoenterprise.web.dto;

import com.kakaoenterprise.domain.user.User;

import lombok.Data;

// Valid 나중에 처리하자.

@Data
public class AuthJoinReqDto {
	private String username;
	private String nickname;
	private String password;
	private String email;
	private String agerange;
	

	public User toEntity() {
		return User.builder()
				.username(username)
				.nickname(nickname)
				.password(password)
				.agerange(agerange)
				.email(email).build();
	}
}
