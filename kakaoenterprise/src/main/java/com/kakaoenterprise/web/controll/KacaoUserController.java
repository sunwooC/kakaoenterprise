package com.kakaoenterprise.web.controll;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.web.dto.KakaoAuthToken;
import com.kakaoenterprise.web.dto.Message;
import com.kakaoenterprise.web.dto.UserUpdateReqDto;
import com.kakaoenterprise.web.exception.ExceptionEnum;
import com.kakaoenterprise.web.service.impl.KakaoServiceImpl;
import com.kakaoenterprise.web.service.impl.RedisUserImpl;
import com.kakaoenterprise.web.service.impl.UserServiceImpl;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * - 사용자 정보 조회 (#req-user-info) - 로그아웃 (#logout) - 연결끊기 (#unlink) - 토큰갱신
 * (#refresh-token)
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class KacaoUserController {
	/*
	 * @Autowired private SessionRegistry sessionRegistry;
	 */
	private final KakaoServiceImpl kakaoServiceImpl;
	private final UserServiceImpl userServiceImpl;
	private final RedisUserImpl redisUserImpl;

	/**
	 * @Method Name : reqUserInfo
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : 조선우
	 * @변경이력 :
	 * @Method 설명 :카카오로 부터 사용자를 조회하는 기능
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "로컬 Id로 카카오 사용자를 조회하는 기능", notes = "Id는 내부 DB정보")
	@GetMapping("/api/v2/user/me/{id}")
	public ResponseEntity<Message> reqUserInfo(@PathVariable(required = true) Long id) {
		User user = userServiceImpl.findById(id);
		if (user == null || !"Kakao".equals(user.getSysid())) {
			return new ResponseEntity<>(new Message(ExceptionEnum.NOT_FOUND_USER.getMessage()),
					ExceptionEnum.NOT_FOUND_USER.getStatus());
		}
		String snsid = user.getUsername().replace("Kakao_", "");
		ResponseEntity<String> kaKaoUserInfo = kakaoServiceImpl.postUserMeAdmin(snsid);
		return new ResponseEntity<>(new Message(kaKaoUserInfo.getBody()), HttpStatus.OK);
	}

	public ResponseEntity<Message> update(@PathVariable int id, @RequestBody @Valid UserUpdateReqDto userUpdateReqDto,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(new Message(""), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(new Message("update"), HttpStatus.OK);
	}

	/**
	 * @Method Name : updateToken
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : 조선우
	 * @변경이력 :
	 * @Method 설명 :
	 * @param id
	 * @return
	 */
	@Transactional
	@ApiOperation(value = "로컬 Id로 사용자의 토큰을 갱신", notes = "Id는 내부 DB정보")
	@PostMapping("/api/v2/user/token/{id}")
	public ResponseEntity<Message> updateToken(@PathVariable(required = true) Long id) {
		User user = userServiceImpl.findById(id);
		if (user == null || !"Kakao".equals(user.getSysid())) {
			return new ResponseEntity<>(new Message(ExceptionEnum.NOT_KAKO_USER.getMessage()), HttpStatus.BAD_REQUEST);
		}
		String refreshToken = user.getRefreshToken();
		String snsid = user.getUsername().replace("Kakao_", "");
		ResponseEntity<KakaoAuthToken> result = kakaoServiceImpl.updateToken(snsid, refreshToken);
		if (result.getStatusCode() == HttpStatus.OK) {
			user.setAccessToekn(result.getBody().getAccess_token());
			userServiceImpl.save(user);
		}
		return new ResponseEntity<>(new Message("update"), HttpStatus.OK);
	}

	/**
	 * @Method Name : unlink
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : 조선우
	 * @변경이력 :
	 * @Method 설명 :ID로 사용자의 연결을 끊는 기능
	 * @param id
	 * @return
	 */
	@Transactional
	@ApiOperation(value = "로컬 Id로 사용자연결끊기", notes = "Id는 내부 DB정보")
	@ApiImplicitParam(name = "id", value = "로컬 ID", required = true)
	@DeleteMapping("/api/v1/user/unlink/{id}")
	public ResponseEntity<Message> unlink(@PathVariable(required = true) String id) {
		User user = userServiceImpl.findById(Long.parseLong(id));
		if (user == null || !"Kakao".equals(user.getSysid())) {
			return new ResponseEntity<>(new Message(ExceptionEnum.NOT_KAKO_USER.getMessage()), HttpStatus.BAD_REQUEST);
		}
		String snsid = user.getUsername().replace("Kakao_", "");
		ResponseEntity<String> result = kakaoServiceImpl.unlinkAdmin(snsid);
		if (result.getStatusCode() == HttpStatus.OK) {
			userServiceImpl.deleteById(user.getId());
			String sessionId = redisUserImpl.logout(user.getUsername());
			boolean logoutCheck = logout(sessionId);
		}
		return new ResponseEntity<>(new Message("delete"), HttpStatus.OK);
	}

	/**
	 * @Method Name : logout
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 :세션Id와 세션을 만료 시는 기능
	 * @param sessionId 세션의 ID
	 * @return 로그인 사용자 강제로그아웃 사용자가 같을때 true
	 */
	private boolean logout(String sessionId) {
		HttpSession session = getCurrentUserAccount(false);
		if (session != null && session.getId().equals(sessionId)) {
			session.removeAttribute("sessionId");
			session.removeAttribute("sessionUserName");
			session.invalidate(); // 세션의 모든 속성을 삭제
			return true;
		}
		return false;
	}

	/**
	 * @Method Name : logout
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 :
	 * @param req
	 * @param id
	 * @return
	 */
	@Transactional
	@ApiOperation(value = "로컬 Id로 사용자연결끊기", notes = "Id는 내부 DB정보")
	@ApiImplicitParam(name = "id", value = "로컬 ID", required = true)
	@PostMapping("/api/v1/user/logout/{id}")
	public ResponseEntity<Message> logout(HttpServletRequest req, @PathVariable(required = true) String id) {
		User user = userServiceImpl.findById(Long.parseLong(id));
		if (user == null || !"Kakao".equals(user.getSysid())) {
			return new ResponseEntity<>(new Message(ExceptionEnum.NOT_FOUND_USER.getMessage()), HttpStatus.BAD_REQUEST);
		}
		String snsid = user.getUsername().replace("Kakao_", "");
		ResponseEntity<String> result = kakaoServiceImpl.logout(user.getAccessToekn());
		if (result.getStatusCode() == HttpStatus.OK) {
			String session = redisUserImpl.logout(user.getUsername());
			boolean logoutCheck = logout(session);
		}
		return new ResponseEntity<>(new Message("logout"), HttpStatus.OK);
	}

	/**
	 * @Method Name : getCurrentUserAccount
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
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
}
