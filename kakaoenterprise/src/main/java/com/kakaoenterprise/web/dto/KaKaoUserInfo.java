package com.kakaoenterprise.web.dto;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 카카오 정보 중 UserInfo
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@Data
public class KaKaoUserInfo {
	private Long id;
	private LocalDateTime connected_at;
	private KakaoProperties properties;
	private KakaoAccount kakao_account;

}
