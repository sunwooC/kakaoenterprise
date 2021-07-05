package com.kakaoenterprise.web.dto;

import com.kakaoenterprise.domain.user.User;

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
	private String username;
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
