package com.kakaoenterprise.web.controll;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.web.dto.KakaoAuthToken;
import com.kakaoenterprise.web.exception.ApiException;
import com.kakaoenterprise.web.exception.ExceptionEnum;
import com.kakaoenterprise.web.service.impl.KakaoServiceImpl;
import com.kakaoenterprise.web.service.impl.RedisUserImpl;
import com.kakaoenterprise.web.service.impl.UserServiceImpl;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
/**
 * - 사용자 정보 조회 (#req-user-info) 
 * - 로그아웃 (#logout) 
 * - 연결끊기 (#unlink) 
 * - 토큰갱신 (#refresh-token)
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
public class KacaoUserController {
	/*
	 * @Autowired private SessionRegistry sessionRegistry;
	 */
	private final KakaoServiceImpl kakaoServiceImpl;
	private final UserServiceImpl userServiceImpl;
	private final RedisUserImpl redisUserImpl;

	/**
	 * @Method Name  : reqUserInfo
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 :
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "Id로 카카오 사용자를 조회하는 기능", notes = "Id는 내부 DB정보")
	@GetMapping("/api/v2/user/me/{id}")
	public ResponseEntity<String> reqUserInfo(@PathVariable(required = true) Long id) {
		User user = userServiceImpl.findById(id);
		if (user == null || !"Kakao".equals(user.getSysid())) {
			throw new ApiException(ExceptionEnum.NOT_FOUND_USER);
		}
		String snsid = user.getUsername().replace("Kakao_", "");
		ResponseEntity<String> kaKaoUserInfo = kakaoServiceImpl.postUserMeAdmin(snsid);
		return kaKaoUserInfo;
	}

	/**
	 * @Method Name  : updateToken
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 :
	 * @param id
	 * @return
	 */
	@Transactional
	@ApiOperation(value = "Id로 사용자의 토큰을 갱신", notes = "Id는 내부 DB정보")
	@PostMapping("/api/v2/user/token/{id}")
	public ResponseEntity<KakaoAuthToken> updateToken(@PathVariable(required = true) Long id) {
		User user = userServiceImpl.findById(id);
		if (user == null || !"Kakao".equals(user.getSysid())) {
			throw new ApiException(ExceptionEnum.NOT_FOUND_USER);
		}
		String refreshToken = user.getRefreshToken();
		String snsid = user.getUsername().replace("Kakao_", "");
		ResponseEntity<KakaoAuthToken> result = kakaoServiceImpl.updateToken(snsid, refreshToken);
		if (result.getStatusCode() == HttpStatus.OK) {
			user.setAccessToekn(result.getBody().getAccess_token());
			userServiceImpl.Save(user);
		}
		return result;
	}

	/**
	 * @Method Name  : unlink
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 :
	 * @param id
	 * @return
	 */
	@Transactional
	@ApiOperation(value = "Id로 사용자연결끊기", notes = "Id는 내부 DB정보")
	@DeleteMapping("/api/v1/user/unlink/{id}")
	public ResponseEntity<String> unlink(@PathVariable(required = true) String id) {
		User user = userServiceImpl.findById(Long.parseLong(id));
		if (user == null || !"Kakao".equals(user.getSysid())) {
			throw new ApiException(ExceptionEnum.NOT_FOUND_USER);
		}
		String snsid = user.getUsername().replace("Kakao_", "");
		ResponseEntity<String> result = kakaoServiceImpl.unlinkAdmin(snsid);
		if (result.getStatusCode() == HttpStatus.OK) {
			userServiceImpl.deleteById(user.getId());
			redisUserImpl.logout(user.getUsername());
		}
		return result;
	}

	/**
	 * @Method Name  : logout
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 :
	 * @param req
	 * @param id
	 * @return
	 */
	@Transactional
	@ApiOperation(value = "Id로 사용자로그아웃", notes = "Id는 내부 DB정보")
	@PostMapping("/api/v1/user/logout/{id}")
	public ResponseEntity<String> logout(HttpServletRequest req, @PathVariable(required = true) String id) {
		User user = userServiceImpl.findById(Long.parseLong(id));
		if (user == null || !"Kakao".equals(user.getSysid())) {
			throw new ApiException(ExceptionEnum.NOT_FOUND_USER);
		}
		String snsid = user.getUsername().replace("Kakao_", "");
		ResponseEntity<String> result = kakaoServiceImpl.logout(user.getAccessToekn());
		if (result.getStatusCode() == HttpStatus.OK) {
			redisUserImpl.logout(user.getUsername());
		}
		return result;
	}

}
