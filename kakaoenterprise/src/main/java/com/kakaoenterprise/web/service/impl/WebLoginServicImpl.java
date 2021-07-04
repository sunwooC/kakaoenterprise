package com.kakaoenterprise.web.service.impl;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.kakaoenterprise.domain.user.RoleType;
import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.web.dto.UserLoginDto;
import com.kakaoenterprise.web.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class WebLoginServicImpl {
	private final UserRepository userRepository;

	public User loadUserByUsername(UserLoginDto user) {
		User info = userRepository.findByUsername(user.getUsername());
		return info;
	}
	
	@Transactional
	public boolean join(User user) {
		User info = userRepository.findByUsername(user.getUsername());
		if (info != null) {
			return false;
		}
		user.setRole(RoleType.USER);
		user.setSysid("LOCAL");
		userRepository.save(user);
		return true;
	}
	@Transactional
	public boolean merge(User user) {
		User info = userRepository.findByUsername(user.getUsername());
		if (info != null) {
			user.setId(info.getId());
		}
		userRepository.save(user);
		return true;
	}

}
