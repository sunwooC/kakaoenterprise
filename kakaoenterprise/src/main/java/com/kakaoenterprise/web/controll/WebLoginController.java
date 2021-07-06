package com.kakaoenterprise.web.controll;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
import com.kakaoenterprise.web.exception.ApiException;
import com.kakaoenterprise.web.exception.ExceptionEnum;
import com.kakaoenterprise.web.service.impl.KakaoUserServiceImpl;
import com.kakaoenterprise.web.service.impl.RedisUserImpl;
import com.kakaoenterprise.web.service.impl.UserServiceImpl;
import com.kakaoenterprise.web.service.impl.WebLoginServicImpl;

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
	 * @Method Name  : login
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 :ㅣlocal 로컬 가입자 로그인
	 * @param user 가입자의 상세 정보
	 * @return
	 */
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
		HttpSession session = getCurrentUserAccount(true);
		session.setAttribute("sessionId", info.getId().toString());
		session.setAttribute("sessionUserName", user.getUsername());
		return new ResponseEntity<>(new Message("login"), HttpStatus.OK);
	}	

	/**
	 * @Method Name  : join
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 : 로그인
	 * @param authJoinReqDto 로그인 사용자의 정보
	 * @return
	 */
	@RequestMapping("/api/v1/auth/local/new")
	public ResponseEntity join(@RequestBody AuthJoinReqDto authJoinReqDto) {
		authJoinReqDto.setPassword(ecodeing(authJoinReqDto.getPassword()));
		User user = userImpl.join(authJoinReqDto.toEntity());
		if (user == null ) {
			throw new ApiException(ExceptionEnum.NOT_JOIN_USER);
		}
		return new ResponseEntity<>(new Message("join"), HttpStatus.OK);
	}
	/**
	 * @Method Name  : join
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 : 로그아웃
	 * @return
	 * @throws IOException 
	 */
	@DeleteMapping("/api/v1/auth/local")
	public ResponseEntity logout(HttpServletResponse response) throws IOException {
		HttpSession session = getCurrentUserAccount(false);
		if (session == null) {
			return new ResponseEntity<>(1, HttpStatus.OK);
		}
		String id = (String)session.getAttribute("sessionId");
		
		User user = userServiceImpl.findById(Long.parseLong(id));
		if (user == null || "Kakao".equals(user.getSysid())) {
			String snsid = user.getUsername().replace("Kakao_", "");
			String sessionId = redisUserImpl.logout(user.getUsername());
		}
		session.removeAttribute("sessionId");
		session.removeAttribute("sessionUserName");
		session.invalidate(); //세션의 모든 속성을 삭제
		return new ResponseEntity<>(new Message("logout"), HttpStatus.OK);
	}

	
	/**
	 * @Method Name  : ecodeing
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 : SHA-256 암호화
	 * @param password 패스워드
	 * @return
	 */
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

	/**
	 * @Method Name  : getCurrentUserAccount
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
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
	 * @Method Name  : callback
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 : 카카오 oauth로그인 처리
	 * @param response 로그인 관련 처리
	 * @param code 일회용 키
	 * @param error 예러 내용
	 * @return
	 * @throws IOException
	 */
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
				.snsid(oAuth2UserInfo.getId())
				.sysid("Kakao").role(RoleType.USER).build();
		user = userImpl.merge(user);
		HttpSession session = getCurrentUserAccount(true);
		session.setAttribute("sessionId", user.getId().toString());
		session.setAttribute("sessionUserName", user.getUsername());
		redisUserImpl.login(user.getUsername(),session.getId());
		
		response.sendRedirect("/user/userlist.html");
		return new ResponseEntity<>(1, HttpStatus.OK);
	}

}
