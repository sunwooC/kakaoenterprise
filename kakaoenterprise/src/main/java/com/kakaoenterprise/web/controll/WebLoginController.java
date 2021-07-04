package com.kakaoenterprise.web.controll;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kakaoenterprise.config.RestConfig;
import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.interceptor.LoginCheckInterceptor;
import com.kakaoenterprise.web.dto.AuthJoinReqDto;
import com.kakaoenterprise.web.dto.KaKaoUserInfo;
import com.kakaoenterprise.web.dto.KakaoAuthToken;
import com.kakaoenterprise.web.dto.UserLoginDto;
import com.kakaoenterprise.web.exception.ApiException;
import com.kakaoenterprise.web.exception.ExceptionEnum;
import com.kakaoenterprise.web.service.WebLoginServicImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class WebLoginController {
	private final WebLoginServicImpl userImpl;
	private final RestConfig restConfig;
	
	
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

	

	@RequestMapping("/api/v1/auth/local")
	public ResponseEntity login(@RequestBody UserLoginDto user){
		User info = userImpl.loadUserByUsername(user);
		if (info == null) {
			throw new ApiException(ExceptionEnum.NOT_FOUND_USER);
		}
		String password = ecodeing(user.getPassword());
		if(!password.equals(info.getPassword())) {
			throw new ApiException(ExceptionEnum.NOT_MATCHED_USER);
		}
		// return "/user/userlist";
		HttpSession session = getCurrentUserAccount();
		session.setAttribute("userInfo", info);
		
		return new ResponseEntity<>(1, HttpStatus.OK);
	}

	
	@RequestMapping("/api/auth/local/new")
	public ResponseEntity join(@RequestBody AuthJoinReqDto authJoinReqDto) {
		
		authJoinReqDto.setPassword(ecodeing(authJoinReqDto.getPassword()));
		boolean result = userImpl.join(authJoinReqDto.toEntity());
		if(result != true) {
			throw new ApiException(ExceptionEnum.NOT_HAS_USER);
		}
		return new ResponseEntity<>(1, HttpStatus.OK);
	}

	
	
	/*
	 * @RequestMapping("/user/userlist") public String userList() { return
	 * "/user/userlist"; }
	 */
	public String ecodeing(String password) {
		String ecodeing = "";
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			ecodeing = String.format("%064x", new BigInteger(1, md.digest()));
		}catch (Exception e) {
		}
		return ecodeing;
	}
	public HttpSession getCurrentUserAccount() {
	    ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession httpSession = servletRequestAttribute.getRequest().getSession(true);
	    return httpSession;
	}
	
	
	@GetMapping("/login/oauth2/code/kakao")
	public String callback(String code, String error) {

		postOauthToken(code);
		return "/user/userlist.html";
	}

	public void postOauthToken(String code) {
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
		//log.info(response.getBody());
		postUserMe(response.getBody());
	}

	public void postUserMe(KakaoAuthToken token) {
		RestTemplate rt = restConfig.restTemplate("");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", contentType);
		headers.add("Authorization", "Bearer " + token.getAccess_token());
		HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(headers);
		ResponseEntity<KaKaoUserInfo> response = rt.exchange(userMeUri, HttpMethod.POST, req, KaKaoUserInfo.class);
		System.out.println(response.getBody());
		// logout(token.getAccess_token());
	}
	
	

}
