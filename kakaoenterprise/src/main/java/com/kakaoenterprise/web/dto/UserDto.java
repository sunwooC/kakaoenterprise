package com.kakaoenterprise.web.dto;

import com.kakaoenterprise.domain.user.User;

import io.swagger.annotations.ApiParam;
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
	@ApiParam(value = "User Idf로 로그인시 사용", required = false, example = "h1212")
	private Long id;
	@ApiParam(value = "닉네임", required = false, example = "하이하이")
	private String nickname;
	@ApiParam(value = "email", required = false, example = "email이나 형식은 무관합니다.")
	private String email;
	@ApiParam(value = "연령대", required = false, example = "10~19")
	private String agerange;
	@ApiParam(value = "SNS시스템 의 고유값", required = false, example = "123")
	private String snsid;
	@ApiParam(value = "SNS시스템 ID", required = false, example = "kakao")
	private String sysid;

	@Builder
	public UserDto(Long id, String nickname, String email, String agerange, String snsid, String sysid,
			String refreshToken, String accessToekn) {
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.agerange = agerange;
		this.snsid = snsid;
		this.sysid = sysid;

	}

	public UserDto(User user) {
		this.id = user.getId();
		this.nickname = user.getNickname();
		this.email = user.getEmail();
		this.agerange = user.getAgerange();
		this.snsid = user.getNickname();
		this.sysid = user.getSysid();

	}
}
