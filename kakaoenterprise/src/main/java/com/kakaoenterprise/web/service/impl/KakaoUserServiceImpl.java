package com.kakaoenterprise.web.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.kakaoenterprise.config.RestConfig;
import com.kakaoenterprise.web.dto.KaKaoUserInfo;
import com.kakaoenterprise.web.dto.KakaoAuthToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 카카오 사용자 로그인 처리
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class KakaoUserServiceImpl {

	/**
	 * 카아오 클라이언트 ID
	 */
	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String clientId;
	/**
	 * 카카오 로그인 이후 디다리엑트 뢷 Url
	 */
	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String redirectUri;

	/**
	 * 카카오 엑세스 토큰갱신 Url
	 */
	@Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
	private String tokenUri;

	/**
	 * 카카오 엑세스 토큰갱신 Url
	 */
	private String contentType = "application/x-www-form-urlencoded;charset=utf-8";

	/**
	 * 카카오 사용자 정보 조회 Url
	 */
	@Value("${kakao.userMeUri}")
	private String userMeUri;

	/**
	 * 카카오 로그아웃 Url
	 */
	@Value("${kakao.userLogoutUri}")
	private String userLogoutUri;

	/**
	 * 카카오 연결끊기 Url
	 */
	@Value("${kakao.userUnlinkUri}")
	private String userUnlinkUri;
	/**
	 * 카카오 어드민 Key
	 */
	@Value("${kakao.adminKey}")
	private String adminKey;

	private final RestConfig restConfig;

	/**
	 * @Method Name : postOauthToken
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 :최초 리다이렉트시 code를 통해 키들을 가져오는 기능
	 * @param code
	 * @return
	 */
	public ResponseEntity<KakaoAuthToken> postOauthToken(String code) {
		RestTemplate rt = restConfig.restTemplate("aaaa");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", clientId);
		params.add("redirect_uri", redirectUri);
		params.add("code", code);
		HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(params, headers);
		ResponseEntity<KakaoAuthToken> response = rt.exchange(tokenUri, HttpMethod.POST, req, KakaoAuthToken.class);
		log.debug("{}", response.getBody());
		return response;
	}

	/**
	 * @Method Name : postUserMe
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 : 프로필 정보를 가져 오는 기능
	 * @param token
	 * @return
	 */
	public ResponseEntity<KaKaoUserInfo> postUserMe(KakaoAuthToken token) {
		RestTemplate rt = restConfig.restTemplate("");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", contentType);
		headers.add("Authorization", "Bearer " + token.getAccess_token());
		HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(headers);
		ResponseEntity<KaKaoUserInfo> response = rt.exchange(userMeUri, HttpMethod.POST, req, KaKaoUserInfo.class);
		log.debug("{}", response.getBody());
		return response;
	}
}
