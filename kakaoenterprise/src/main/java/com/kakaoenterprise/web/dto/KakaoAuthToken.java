package com.kakaoenterprise.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KakaoAuthToken {
	String token_type;
	String access_token;
	long expires_in;
	String refresh_token;
	long refresh_token_expires_in;
	
}
