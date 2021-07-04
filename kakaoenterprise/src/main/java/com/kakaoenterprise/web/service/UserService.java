package com.kakaoenterprise.web.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.web.dto.UserDto;
import com.kakaoenterprise.web.dto.UserUpdateReqDto;

public interface UserService {

	public Page<UserDto> findAll(Pageable pageable);

	public Page<UserDto> findbyAgerange(String Argrang, Pageable pageable);

	public Page<UserDto> findByEmailEndingWith(String daomin, Pageable pageable);

	public Page<UserDto> findByEmailEndingWithAndAgerange(String argrang, String daomin, Pageable pageable);

	public void update(UserUpdateReqDto account);

	public void deleteById(Long id);

	public void deleteBySnsid(Long id);

	public User findById(Long id);

}
