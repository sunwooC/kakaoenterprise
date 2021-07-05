package com.kakaoenterprise.web.dto;

import com.kakaoenterprise.domain.user.User;

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
	private Long id;
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
