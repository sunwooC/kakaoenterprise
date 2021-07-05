package com.kakaoenterprise.web.dto;

import com.kakaoenterprise.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 조회된 User에 대해 클라이언트로 전달할 DTO
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@Getter
@NoArgsConstructor
public class UserDto {
	private Long id;
	private String nickname;
	private String email;
	private String agerange;
	private String snsid;
	private String sysid;
	private Integer agegrop;
	private String refreshToken;
	private String accessToekn;
	
	@Builder
	public UserDto(Long id, String nickname, String email, String agerange, String snsid, String sysid, Integer agegrop,String refreshToken,String accessToekn) {
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.agerange = agerange;
		this.snsid  = snsid;
		this.sysid  = sysid;
		this.agegrop  = agegrop;
		this.refreshToken  = refreshToken;
		this.accessToekn  = accessToekn;

	}

	public UserDto(User user) {
		this.id = user.getId();
		this.nickname = user.getNickname();
		this.email = user.getEmail();
		this.agerange = user.getAgerange();
		this.snsid = user.getNickname();
		this.sysid = user.getSysid();
		this.agegrop = user.getAgegrop();
		this.refreshToken = user.getRefreshToken();
		this.accessToekn = user.getAccessToekn();

	}
}
