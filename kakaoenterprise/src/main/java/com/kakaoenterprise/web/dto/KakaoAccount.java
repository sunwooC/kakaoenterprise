package com.kakaoenterprise.web.dto;

import lombok.Data;

/**
 * 카카오 로그인시 Accout관련 정보
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@Data
public class KakaoAccount {
	private boolean profile_needs_agreement;
	private KakaoProfile profile;
	private boolean has_email;
	private boolean email_needs_agreement;
	private boolean is_email_valid;
	private boolean is_email_verified;
	private String email;
	private boolean has_age_range;
	private boolean age_range_needs_agreement;
	private String age_range;
}
