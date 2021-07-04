package com.kakaoenterprise.web.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.kakaoenterprise.config.RestConfig;

import org.springframework.util.LinkedMultiValueMap;

import com.kakaoenterprise.web.dto.KaKaoUserInfo;
import com.kakaoenterprise.web.dto.KakaoAuthToken;

import lombok.extern.slf4j.Slf4j;

	

/**
 * 카카오 요청 처리 Impl
 * @author sunwoo cho
 */

@Slf4j
@Service
@Transactional
public class KakaoServiceImpl {
	
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

	/**
     * RestTemplate config 처리 Bean 
     */
	@Autowired
	private RestConfig restConfig;

    /**
     * 사용자 정보 조회(Admin Key로)하는 기능
     * @param snsid 조회하고자 하는 사용자의 카카오번호
     * @return 응답내용 반환
     */
	public ResponseEntity<KaKaoUserInfo> postUserMe(String snsid) {
		RestTemplate rt = restConfig.restTemplate(this.getClass().getName());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type"	, contentType);
		headers.add("Authorization"	, "KakaoAK " + adminKey);
		HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(headers);
		ResponseEntity<KaKaoUserInfo> response = rt.exchange(userMeUri, HttpMethod.POST, req, KaKaoUserInfo.class);
		return response;
	}
	
    /**
     * 카카오 사용자 정보 조회(Admin Key로)하는 기능
     * @param snsid 조회하고자 하는 사용자의 카카오번호
     * @return 응답내용 반환
     */
	public ResponseEntity<String> postUserMeAdmin(String snsid) {
		RestTemplate rt = restConfig.restTemplate(this.getClass().getName());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type"	, contentType);
		headers.add("Authorization"	, "KakaoAK " + adminKey);
		HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(headers);
		String param = userMeUri + "?target_id_type=user_id&target_id=" + snsid;
		ResponseEntity<String> response = rt.exchange(param, HttpMethod.POST, req, String.class);
		return response;
	}
    /**
     *  카카오 사용자 엑세스 토큰 Refresh 기능
     * @param snsid 조회하고자 하는 사용자의 카카오번호
     * @param refreshToken 조회하고자 하는 사용자의 리플레스 토큰
     * @return 응답내용 반환
     */
	public ResponseEntity<KakaoAuthToken> updateToken(String snsid, String refreshToken) {
		RestTemplate rt = restConfig.restTemplate(this.getClass().getName());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type"	, contentType);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type"		, "refresh_token");
		params.add("client_id"		, clientId);
		params.add("refresh_token", refreshToken);
		HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(params, headers);
		ResponseEntity<KakaoAuthToken> response = rt.exchange(tokenUri, HttpMethod.POST, req, KakaoAuthToken.class);
		return response;
	}

    /**
     *  카카오 사용자 연결끊기 기능
     * @param snsid 조회하고자 하는 사용자의 카카오번호
     * @return 응답내용 반환
     */
	public ResponseEntity<String> unlinkAdmin(String snsid) {
		RestTemplate rt = restConfig.restTemplate(this.getClass().getName());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type"	, contentType);
		headers.add("Authorization"	, "KakaoAK " + adminKey);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("target_id_type"	, "user_id");
		params.add("target_id"		, snsid);
		HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = rt.exchange(userUnlinkUri, HttpMethod.POST, req, String.class);
		return response;
	}

	
    /**
     *  카카오 사용자 어드민 토크으로 로그아웃 기능
     * @param snsid 조회하고자 하는 사용자의 카카오번호
     * @return 응답내용 반환
     */
	public ResponseEntity<String>  logoutAdmin(String snsid) {
		RestTemplate rt = restConfig.restTemplate(this.getClass().getName());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type"	, contentType);
		headers.add("Authorization", "KakaoAK " + adminKey);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("target_id_type"	, "user_id");
		params.add("target_id"		, snsid);
		HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(headers);
		ResponseEntity<String> response = rt.exchange(userLogoutUri, HttpMethod.POST, req, String.class);
		return response;
	}
    /**
     *  카카오 사용자 어드민 토크으로 로그아웃 기능
     * @param accessTonek 로그아웃해야하는 사용자의 토큰
     * @return 응답내용 반환
     */
	public ResponseEntity<String>  logout(String accessTonek) {
		RestTemplate rt = restConfig.restTemplate(this.getClass().getName());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type"	, contentType);
		headers.add("Authorization", "Bearer " + accessTonek);
		HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(headers);
		ResponseEntity<String> response = rt.exchange(userLogoutUri, HttpMethod.POST, req, String.class);
		return response;
	}
	
}
