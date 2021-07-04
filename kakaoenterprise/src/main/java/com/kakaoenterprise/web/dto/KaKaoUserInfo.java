package com.kakaoenterprise.web.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class KaKaoUserInfo {
	private Long id;
	private LocalDateTime connected_at;
	private KakaoProperties properties;
	private KakaoAccount kakao_account;

}
