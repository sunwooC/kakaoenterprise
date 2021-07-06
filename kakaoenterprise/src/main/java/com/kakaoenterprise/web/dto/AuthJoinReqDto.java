package com.kakaoenterprise.web.dto;

import javax.validation.constraints.NotBlank;

import com.kakaoenterprise.domain.user.User;

import io.swagger.annotations.ApiParam;
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
	@ApiParam(value = "User Idf로 로그인시 사용", required = false, example = "h1212")
	@NotBlank(message = "User Id는 필수 입니다.")
	private String username;
	@ApiParam(value = "닉네임", required = false, example = "하이하이")
	private String nickname;
	@ApiParam(value = "패스워드", required = false, example = "abc1234")
	@NotBlank(message = "패스워드는 필수 입니다.")
	private String password;
	@ApiParam(value = "email", required = false, example = "email이나 형식은 무관합니다.")
	private String email;
	@ApiParam(value = "연령대", required = false, example = "10~19")
	@NotBlank(message = "나이 범위는 필수 입니다.")
	private String agerange;

	public User toEntity() {
		return User.builder().username(username).nickname(nickname).password(password).agerange(agerange).email(email)
				.build();
	}
}
