package com.kakaoenterprise.web.service.impl;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.kakaoenterprise.domain.user.RoleType;
import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.web.dto.UserLoginDto;
import com.kakaoenterprise.web.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * 웹로그인시 로그인 관련 사용자 정보를 조회 및 생성,변경하는 기능
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
@Transactional
public class WebLoginServicImpl {
	private final UserRepository userRepository;

	/**
	 * @Method Name : loadUserByUsername
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 :사용자 정보를 조회하는 기능
	 * @param user
	 * @return
	 */
	public User loadUserByUsername(UserLoginDto user) {
		User info = userRepository.findByUsername(user.getUsername());
		return info;
	}

	/**
	 * @Method Name : join
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 : 사용자를 추가하는 기능
	 * @param user
	 * @return
	 */
	@Transactional
	public User join(User user) {
		User info = userRepository.findByUsername(user.getUsername());
		if (info != null) {
			return null;
		}
		user.setRole(RoleType.USER);
		user.setSysid("LOCAL");
		return userRepository.save(user);
	}

	/**
	 * @Method Name : merge
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 : 기존 유저 정보를 변경하는 기능
	 * @param user
	 * @return
	 */
	@Transactional
	public User merge(User user) {
		User info = userRepository.findByUsername(user.getUsername());
		if (info != null) {
			user.setId(info.getId());
		}
		return userRepository.save(user);
	}

}
