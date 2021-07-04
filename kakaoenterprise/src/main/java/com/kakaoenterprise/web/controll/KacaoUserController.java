package com.kakaoenterprise.web.controll;

import java.util.List;

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
 */
public class KacaoUserController {
	/*
	@Autowired
	private SessionRegistry sessionRegistry;
	 */
	private final KakaoServiceImpl kakaoServiceImpl;
	private final UserServiceImpl userServiceImpl;

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
			/*
			userServiceImpl.deleteById(user.getId());
			List list = sessionRegistry.getAllPrincipals();
			for (Object item : sessionRegistry.getAllPrincipals()) {
				System.out.println(item);
			}
			*/
		}
		return result;
	}
	
	@Transactional
	@ApiOperation(value = "Id로 사용자연결끊기", notes = "Id는 내부 DB정보")
	@PostMapping("/api/v1/user/logout/{id}")
	public ResponseEntity<String> logout(@PathVariable(required = true) String id) {
		User user = userServiceImpl.findById(Long.parseLong(id));
		if (user == null || !"Kakao".equals(user.getSysid())) {
			throw new ApiException(ExceptionEnum.NOT_FOUND_USER);
		}
		String snsid = user.getUsername().replace("Kakao_", "");
		ResponseEntity<String> result = kakaoServiceImpl.logoutAdmin(snsid);
		if (result.getStatusCode() == HttpStatus.OK) {
			/*
			userServiceImpl.deleteById(user.getId());
			List list = sessionRegistry.getAllPrincipals();
			for (Object item : sessionRegistry.getAllPrincipals()) {
				System.out.println(item);
			}
			*/
		}
		return result;
	}

}
