package com.kakaoenterprise.web.dto;

import lombok.Data;

/**
 * 카카오 정보 중 Profile 
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@Data
public class KakaoProfile {
	private String nickname;
	private String thumbnail_image_url;
	private String profile_image_url;
	private boolean is_default_image;
}