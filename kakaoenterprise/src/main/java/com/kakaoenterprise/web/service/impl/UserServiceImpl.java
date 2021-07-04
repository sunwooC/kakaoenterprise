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

@Service
@Transactional
public class UserServiceImpl  {
	@Autowired
	private UserRepository userRepository;


	
    /**
     * 카카오 사용자 정보 조회(Admin Key로)하는 기능
     * @param snsid 조회하고자 하는 사용자의 카카오번호
     * @return 응답내용 반환
     */
	//@Override
	public Page<UserDto> findAll(Pageable pageable) {
		Page<User> users = userRepository.findAll(pageable);
		return users.map(UserDto::new);
	}

	//@Override
	public Page<UserDto> findbyAgerange(String agerang, Pageable pageable) {
		Page<User> users = userRepository.findByAgerange(agerang, pageable);
		return users.map(UserDto::new);
	}

	//@Override
	public Page<UserDto> findByEmailEndingWith(String daomin, Pageable pageable) {
		Page<User> users = userRepository.findByEmailEndingWith(daomin, pageable);
		return users.map(UserDto::new);
	}

	//@Override
	public Page<UserDto> findByEmailEndingWithAndAgerange(String agerang, String daomin, Pageable pageable) {
		Page<User> users = userRepository.findByEmailEndingWithAndAgerange(daomin, agerang, pageable);
		return users.map(UserDto::new);
	}

	//@Override
	public User findById(Long id) {
		Optional<User> user = userRepository.findById(id);
		return user.orElse(null);
	}

	//@Override
	public void update(UserUpdateReqDto account) {
		userRepository.update(account.getNickname(), account.getId());
	}

	//@Override
	public void deleteBySnsid(Long id) {
		userRepository.deleteBySnsid(id);
	}

	//@Override
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}
	public void Save(User user) {
		userRepository.save(user);
	}
	

}
