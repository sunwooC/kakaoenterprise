package com.kakaoenterprise.web.dto;

import lombok.Data;

@Data
public class KakaoAccount {
	private boolean profile_needs_agreement;
	private KakaoProfile profile;
	
	private boolean has_email; 
	private boolean email_needs_agreement; 
	private boolean is_email_valid; 
	private boolean is_email_verified; 
	private String  email; 
	private boolean has_age_range; 
	private boolean age_range_needs_agreement; 
	private String  age_range; 
}
