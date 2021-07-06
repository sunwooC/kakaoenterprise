package com.kakaoenterprise.web.dto;

import javax.validation.constraints.NotBlank;

import com.kakaoenterprise.domain.user.User;

import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보 중 nickname를 변경하기 위한 DTO
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */

@Getter
@NoArgsConstructor
public class UserUpdateReqDto {
	@ApiParam(value = "User Idf로 로그인시 사용", required = false, example = "h1212")
	private Long id;
	@ApiParam(value = "닉네임", required = false, example = "하이하이")
	@NotBlank(message = "닉네임은 필수 입니다.")
	private String nickname;

	@Builder
	public UserUpdateReqDto(Long id, String nickname) {
		this.id = id;
		this.nickname = nickname;
	}

	public UserUpdateReqDto(User user) {
		this.id = user.getId();
		this.nickname = user.getNickname();

	}
}
