package com.kakaoenterprise.web.controll;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
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
import com.kakaoenterprise.domain.user.RoleType;
import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.interceptor.LoginCheckInterceptor;
import com.kakaoenterprise.web.dto.AuthJoinReqDto;
import com.kakaoenterprise.web.dto.KaKaoUserInfo;
import com.kakaoenterprise.web.dto.KakaoAuthToken;
import com.kakaoenterprise.web.dto.UserLoginDto;
import com.kakaoenterprise.web.exception.ApiException;
import com.kakaoenterprise.web.exception.ExceptionEnum;
import com.kakaoenterprise.web.service.impl.KakaoUserServiceImpl;
import com.kakaoenterprise.web.service.impl.WebLoginServicImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class WebLoginController {
	private final WebLoginServicImpl userImpl;
	private final KakaoUserServiceImpl kakaoUserServiceImpl;

	@RequestMapping("/api/v1/auth/local")
	public ResponseEntity login(@RequestBody UserLoginDto user) {
		User info = userImpl.loadUserByUsername(user);
		if (info == null) {
			throw new ApiException(ExceptionEnum.NOT_FOUND_USER);
		}
		String password = ecodeing(user.getPassword());
		if (!password.equals(info.getPassword())) {
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
		if (result != true) {
			throw new ApiException(ExceptionEnum.NOT_HAS_USER);
		}
		return new ResponseEntity<>(1, HttpStatus.OK);
	}

	public String ecodeing(String password) {
		String ecodeing = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			ecodeing = String.format("%064x", new BigInteger(1, md.digest()));
		} catch (Exception e) {
		}
		return ecodeing;
	}

	public HttpSession getCurrentUserAccount() {
		ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
		HttpSession httpSession = servletRequestAttribute.getRequest().getSession(true);
		return httpSession;
	}

	@GetMapping("/login/oauth2/code/kakao")
	public ResponseEntity callback(HttpServletResponse response,String code, String error) throws IOException {
		ResponseEntity<KakaoAuthToken> reesutl = kakaoUserServiceImpl.postOauthToken(code);
		if (reesutl.getStatusCode() != HttpStatus.OK) {

		}
		KakaoAuthToken tonek = reesutl.getBody();
		ResponseEntity<KaKaoUserInfo> resUseMe = kakaoUserServiceImpl.postUserMe(tonek);
		if (resUseMe.getStatusCode() != HttpStatus.OK) {

		}
		KaKaoUserInfo oAuth2UserInfo = resUseMe.getBody();
		String id = "Kakao_" + oAuth2UserInfo.getId();
		String nickname = oAuth2UserInfo.getProperties().getNickname() ;
		String ageRange = oAuth2UserInfo.getKakao_account().getAge_range();
		String email = oAuth2UserInfo.getKakao_account().getEmail();
		String refreshToken = tonek.getRefresh_token();
		String accessToken = tonek.getAccess_token();
		
		User user = User.builder().username(id).nickname(nickname)
				.agerange(ageRange).password("=======").email(email)
				.refreshToken(refreshToken).accessToekn(accessToken)
				.sysid("Kakao").role(RoleType.USER).build();
		userImpl.merge(user);
		HttpSession session = getCurrentUserAccount();
		session.setAttribute("userInfo", user);
		response.sendRedirect("/user/userlist.html");
		return new ResponseEntity<>(1, HttpStatus.OK);
	}

}
