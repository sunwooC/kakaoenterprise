package com.kakaoenterprise.web.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.web.dto.UserDto;
import com.kakaoenterprise.web.dto.UserUpdateReqDto;
import com.kakaoenterprise.web.repository.UserRepository;
import com.kakaoenterprise.web.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl  {
	private final UserRepository userRepository;

	
    /**
     * 로컬 및 카카오로 가입된 사용자 정보 조회 페이징 기능
     * @param Pageable 조회하고자 하는 데이터의 정렬 및 페이징 정의
     * @return Pageable에서 정의한 범위 데이터 반환
     */
	public Page<UserDto> findAll(Pageable pageable) {
		Page<User> users = userRepository.findAll(pageable);
		return users.map(UserDto::new);
	}
	
    /**
     * 로컬 및 카카오로 가입된 사용자 정보 조회(연려댕) 페이징 기능
     * @param agerang 연령대 (10~19)
     * @param Pageable 조회하고자 하는 데이터의 정렬 및 페이징 정의
     * @return Pageable에서 정의한 범위 데이터 반환
     */
	public Page<UserDto> findbyAgerange(String agerang, Pageable pageable) {
		Page<User> users = userRepository.findByAgerange(agerang, pageable);
		return users.map(UserDto::new);
	}
	
    /**
     * 로컬 및 카카오로 가입된 사용자 정보 조회(도메인) 페이징 기능
     * @param daomin 이메일의 뒷 부분
     * @param Pageable 조회하고자 하는 데이터의 정렬 및 페이징 정의
     * @return Pageable에서 정의한 범위 데이터 반환
     */
	public Page<UserDto> findByEmailEndingWith(String daomin, Pageable pageable) {
		Page<User> users = userRepository.findByEmailEndingWith(daomin, pageable);
		return users.map(UserDto::new);
	}


    /**
     * 로컬 및 카카오로 가입된 사용자 정보 조회(도메인+연령대) 페이징 기능
     * @param agerang 연령대 (10~19)
     * @param daomin 이메일의 뒷 부분
     * @param Pageable 조회하고자 하는 데이터의 정렬 및 페이징 정의
     * @return Pageable에서 정의한 범위 데이터 반환
     */
	public Page<UserDto> findByEmailEndingWithAndAgerange(String agerang, String daomin, Pageable pageable) {
		Page<User> users = userRepository.findByEmailEndingWithAndAgerange(agerang, daomin, pageable);
		return users.map(UserDto::new);
	}

	/**
	 * 로컬 및 카카오로 가입된 사용자 정보를 고유ID로 조회 
	 * @param id 유저정보의 ID
	 * @return 사용자 정보
	 */
	public User findById(Long id) {
		Optional<User> user = userRepository.findById(id);
		return user.orElse(null);
	}

	/**
	 * 로컬 및 카카오로 가입된 사용자 정보를 고유ID에 대해 nickname변경
	 * @param account 업데이트 대사장의 정보(nickname만) 
	 * @return 업데이트된 카운트
	 */
	public int update(UserUpdateReqDto account) {
		return userRepository.update(account.getNickname(), account.getId());
	}
	
	/**
	 * 로컬 및 카카오로 가입된 사용자 정보를중 snsid에 대해 삭제
	 * @param id 고유ID
	 * @return 삭제된 카운트
	 */
	public int deleteBySnsid(Long id) {
		return userRepository.deleteBySnsid(id);
	}

	/**
	 * 로컬 및 카카오로 가입된 사용자 정보를 고유ID에 대해 삭제
	 * @param id 고유ID
	 * @return 삭제된 카운트
	 */
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}
	/**
	 * 로컬 및 카카오로 가입된 사용자 정보를 저장,변경하
	 * @param user 사용자의 저장하고자하는 정보
	 * @return 정장된 사용자 정보
	 */
	public User Save(User user) {
		return userRepository.save(user);
	}
	

}
