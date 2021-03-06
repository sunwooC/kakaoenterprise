package com.kakaoenterprise.web.dto;

import javax.validation.constraints.NotBlank;

import com.kakaoenterprise.domain.user.User;

import io.swagger.annotations.ApiParam;
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
	@ApiParam(value = "User Idf로 로그인시 사용", required = false, example = "h1212")
	@NotBlank(message = "UserId는 필수 입니다.")
	private String username;
	@ApiParam(value = "패스워드", required = false, example = "abc1234")
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
