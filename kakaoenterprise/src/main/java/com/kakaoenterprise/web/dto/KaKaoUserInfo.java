package com.kakaoenterprise.web.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class KaKaoUserInfo {
	private Long id;
	private LocalDateTime connected_at;
	Properties properties;
	Kakao_account kakao_account;
}
@Data
class Properties {
	private String nickname;
	private String profile_image;
	private String thumbnail_image;
}
@Data
class Kakao_account {
	private boolean profile_needs_agreement;
	private Profile profile;
	
	private boolean has_email; 
	private boolean email_needs_agreement; 
	private boolean is_email_valid; 
	private boolean is_email_verified; 
	private String  email; 
	private boolean has_age_range; 
	private boolean age_range_needs_agreement; 
	private String  age_range; 
    	
}
@Data
class Profile {
	private String nickname;
	private String thumbnail_image_url;
	private String profile_image_url;
	private boolean is_default_image;
}