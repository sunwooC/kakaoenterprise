package com.kakaoenterprise.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class KakaoProperties {
	private String nickname;
	private String profile_image;
	private String thumbnail_image;
}
