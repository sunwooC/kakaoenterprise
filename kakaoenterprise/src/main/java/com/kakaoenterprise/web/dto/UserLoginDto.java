package com.kakaoenterprise.web.dto;

import javax.validation.constraints.NotBlank;

import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.web.exception.ExceptionEnum;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인시 사용되는 DTO
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@Getter
@NoArgsConstructor
public class UserLoginDto {
	@NotBlank(message = "UserId는 필수 입니다.")
	private String username;
	@NotBlank(message = "패스워드는 필수 입니다.")
	private String password;

	@Builder
	public UserLoginDto(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public UserLoginDto(User user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
	}
}
