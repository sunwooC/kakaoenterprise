package com.kakaoenterprise.web.controll;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kakaoenterprise.domain.user.RoleType;
import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.web.dto.AuthJoinReqDto;
import com.kakaoenterprise.web.dto.KaKaoUserInfo;
import com.kakaoenterprise.web.dto.KakaoAuthToken;
import com.kakaoenterprise.web.dto.Message;
import com.kakaoenterprise.web.dto.UserLoginDto;
import com.kakaoenterprise.web.exception.ExceptionEnum;
import com.kakaoenterprise.web.service.impl.KakaoUserServiceImpl;
import com.kakaoenterprise.web.service.impl.RedisUserImpl;
import com.kakaoenterprise.web.service.impl.UserServiceImpl;
import com.kakaoenterprise.web.service.impl.WebLoginServicImpl;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Local 및 카카오 가입자에 대한 로그인 처리
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class WebLoginController {
	private final WebLoginServicImpl userImpl;
	private final KakaoUserServiceImpl kakaoUserServiceImpl;
	private final RedisUserImpl redisUserImpl;
	private final UserServiceImpl userServiceImpl;

	/**
	 * 카카오 인증 요청 URL
	 */
	@Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
	private String authorizationUri;

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
	 * @Method Name : login
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 :ㅣlocal 로컬 가입자 로그인
	 * @param user 가입자의 상세 정보
	 * @return
	 */

	@ApiOperation(value = "로컨 회원 로그인", notes = "ID/PW로 로그인 하는 정보")
	@PostMapping("/api/v1/auth/local")
	public ResponseEntity<Message> login(@RequestBody @Valid UserLoginDto user, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(new Message(result.getAllErrors().get(0).getDefaultMessage()),
					HttpStatus.BAD_REQUEST);
		}

		User info = userImpl.loadUserByUsername(user);
		if (info == null) {
			return new ResponseEntity<>(new Message(ExceptionEnum.NOT_FOUND_USER.getMessage()),
					ExceptionEnum.NOT_FOUND_USER.getStatus());
		}
		// return "/user/userlist";
		HttpSession session = getCurrentUserAccount(true);
		session.setAttribute("sessionId", info.getId().toString());
		session.setAttribute("sessionUserName", user.getUsername());
		return new ResponseEntity<>(new Message("login"), HttpStatus.OK);
	}

	/**
	 * @Method Name : join
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : 조선우
	 * @변경이력 :
	 * @Method 설명 : 신규 사용자를 등록하는 기능
	 * @param authJoinReqDto 로그인 사용자의 정보
	 * @param result         휴요값 인증 처리
	 * @return 신규 생성 결과
	 */
	@ApiOperation(value = "로컨 회원 가입", notes = "신규 가입을 위한 요청 처리")
	@PostMapping("/api/v1/auth/local/new")
	public ResponseEntity<Message> join(@RequestBody @Valid AuthJoinReqDto authJoinReqDto, BindingResult result) {
		if (result.hasErrors()) { 
			return new ResponseEntity<>(new Message(result.getAllErrors().get(0).getDefaultMessage()),
					HttpStatus.BAD_REQUEST);
		}
		authJoinReqDto.setPassword(ecodeing(authJoinReqDto.getPassword()));
		User user = userImpl.join(authJoinReqDto.toEntity());
		if (user == null) {
			return new ResponseEntity<>(new Message(ExceptionEnum.NOT_JOIN_USER.getMessage()),
					ExceptionEnum.NOT_JOIN_USER.getStatus());
		}
		return new ResponseEntity<>(new Message("join"), HttpStatus.OK);
	}

	/**
	 * @Method Name : logout
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : 조선우
	 * @변경이력 :
	 * @Method 설명 : 현재 사용자의 세션을 종료시ㅣ는 기능
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "로컨 회원 로그아웃", notes = "로그인 된 상태에서 로그아웃 됨.")
	@DeleteMapping("/api/v1/auth/local")
	public ResponseEntity<Message> logout() throws IOException {
		HttpSession session = getCurrentUserAccount(false);
		if (session == null) {
			return new ResponseEntity<>(new Message("logout"), HttpStatus.OK);
		}
		String id = (String) session.getAttribute("sessionId");

		User user = userServiceImpl.findById(Long.parseLong(id));
		if (user == null || "Kakao".equals(user.getSysid())) {
			String snsid = user.getUsername().replace("Kakao_", "");
			String sessionId = redisUserImpl.logout(user.getUsername());
		}
		session.removeAttribute("sessionId");
		session.removeAttribute("sessionUserName");
		session.invalidate(); // 세션의 모든 속성을 삭제
		return new ResponseEntity<>(new Message("logout"), HttpStatus.OK);
	}

	/**
	 * @Method Name : getCurrentUserAccount
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : 조선우
	 * @변경이력 :
	 * @Method 설명 : 세션정보를 반환
	 * @return sessionChk : 값에 따라 세션 생성 여부 결정
	 */
	public HttpSession getCurrentUserAccount(boolean sessionChk) {
		ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
		HttpSession httpSession = servletRequestAttribute.getRequest().getSession(sessionChk);
		return httpSession;
	}

	/**
	 * @Method Name : callback
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : 조선우
	 * @변경이력 :
	 * @Method 설명 : 카카오 oauth로그인 처리
	 * @param response 로그인 관련 처리
	 * @param code     일회용 키
	 * @param error    예러 내용
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "카카오 로그인 이후 CallBack 처리", notes = "로그인 및 자동 가입 처리")
	@GetMapping("/login/oauth2/code/kakao")
	public ResponseEntity<Message> callback(HttpServletResponse response, String code, String error)
			throws IOException {
		ResponseEntity<KakaoAuthToken> reesutl = kakaoUserServiceImpl.postOauthToken(code);
		if (reesutl.getStatusCode() != HttpStatus.OK) {
			// log.info("[LOGIN.CODE]statusL{},eror:{}", reesutl.getStatusCode(), error);
			return new ResponseEntity<>(new Message("카카오로부터 CODE를 받지 못함."), HttpStatus.BAD_REQUEST);
		}
		KakaoAuthToken tonek = reesutl.getBody();
		ResponseEntity<KaKaoUserInfo> resUseMe = kakaoUserServiceImpl.postUserMe(tonek);
		if (resUseMe.getStatusCode() != HttpStatus.OK) {
			// log.info("[LOGIN.UESR_US]statusL{},eror:{}", reesutl.getStatusCode(), error);
			return new ResponseEntity<>(new Message(ExceptionEnum.NOT_FOUND_USER.getMessage()),
					ExceptionEnum.NOT_FOUND_USER.getStatus());

		}
		KaKaoUserInfo oAuth2UserInfo = resUseMe.getBody();
		String id = "Kakao_" + oAuth2UserInfo.getId();
		String nickname = oAuth2UserInfo.getProperties().getNickname();
		String ageRange = oAuth2UserInfo.getKakao_account().getAge_range();
		String email = oAuth2UserInfo.getKakao_account().getEmail();
		String refreshToken = tonek.getRefresh_token();
		String accessToken = tonek.getAccess_token();

		User user = User.builder().username(id).nickname(nickname).agerange(ageRange).password("=======").email(email)
				.refreshToken(refreshToken).accessToekn(accessToken).snsid(oAuth2UserInfo.getId()).sysid("Kakao")
				.role(RoleType.USER).build();
		user = userImpl.merge(user);
		HttpSession session = getCurrentUserAccount(true);
		session.setAttribute("sessionId", user.getId().toString());
		session.setAttribute("sessionUserName", user.getUsername());
		redisUserImpl.login(user.getUsername(), session.getId());
		response.sendRedirect("/user/userlist.html");
		return new ResponseEntity<>(new Message("login"), HttpStatus.OK);
	}

	/**
	 * @Method Name : kakaoUrl
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : 조선우
	 * @변경이력 :
	 * @Method 설명 : 카카오 로그인 url 전달용
	 * @returnresponse 로그인 url
	 * @throws IOException
	 */
	@ApiOperation(value = "카카오 로그인 url 전달용", notes = "로그인 화면에 카카오 로그인처리용 url전달")
	@GetMapping("/login/oauth2/kakao/url")
	public ResponseEntity<Message> kakaoUrl() throws IOException {
		StringBuffer sub = new StringBuffer();
		sub.append(authorizationUri).append("?client_id=").append(clientId).append("&redirect_uri=").append(redirectUri)
				.append("&response_type=code");
		return new ResponseEntity<>(new Message(sub.toString()), HttpStatus.OK);
	}

	/**
	 * @Method Name : ecodeing
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : 조선우
	 * @변경이력 :
	 * @Method 설명 : SHA-256 암호화
	 * @param password 패스워드
	 * @return
	 */
	private String ecodeing(String password) {
		String ecodeing = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			ecodeing = String.format("%064x", new BigInteger(1, md.digest()));
		} catch (Exception e) {
		}
		return ecodeing;
	}

}
