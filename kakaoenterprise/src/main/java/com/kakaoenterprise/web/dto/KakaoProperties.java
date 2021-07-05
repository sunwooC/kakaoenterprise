package com.kakaoenterprise.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 카카오 정보 중 Properties 
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class KakaoProperties {
	private String nickname;
	private String profile_image;
	private String thumbnail_image;
}
