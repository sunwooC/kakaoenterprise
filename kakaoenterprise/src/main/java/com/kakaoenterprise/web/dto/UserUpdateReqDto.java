package com.kakaoenterprise.web.dto;

import com.kakaoenterprise.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
