package com.kakaoenterprise.web.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.web.dto.UserDto;
import com.kakaoenterprise.web.dto.UserUpdateReqDto;

/**
 * 사용자 정보를 생성,변경, 삭제하는 기능의 서비스 정의
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
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
