package com.kakaoenterprise.web.dto;

import javax.validation.constraints.NotBlank;

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
	@NotBlank(message = "User Id는 필수 입니다.")
	private String username;
	private String nickname;
	@NotBlank(message = "패스워드는 필수 입니다.")
	private String password;
	private String email;
	@NotBlank(message = "나이 범위는 필수 입니다.")
	private String agerange;

	public User toEntity() {
		return User.builder().username(username).nickname(nickname).password(password).agerange(agerange).email(email)
				.build();
	}
}
