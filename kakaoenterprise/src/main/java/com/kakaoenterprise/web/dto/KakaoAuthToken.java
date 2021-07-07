package com.kakaoenterprise.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 카카오 로그인 시 관리되는 토큰 정보
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class KakaoAuthToken {
	String token_type;
	String access_token;
	long expires_in;
	String refresh_token;
	long refresh_token_expires_in;

}
