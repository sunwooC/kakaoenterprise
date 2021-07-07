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

	/**
	 * @Method Name : findAll
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 :
	 * @param pageable
	 * @return
	 */
	public Page<UserDto> findAll(Pageable pageable);

	/**
	 * @Method Name : findbyAgerange
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 :
	 * @param Argrang
	 * @param pageable
	 * @return
	 */
	public Page<UserDto> findbyAgerange(String Argrang, Pageable pageable);

	/**
	 * @Method Name : findByEmailEndingWith
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 :
	 * @param daomin
	 * @param pageable
	 * @return
	 */
	public Page<UserDto> findByEmailEndingWith(String daomin, Pageable pageable);

	/**
	 * @Method Name : findByEmailEndingWithAndAgerange
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 :
	 * @param argrang
	 * @param daomin
	 * @param pageable
	 * @return
	 */
	public Page<UserDto> findByEmailEndingWithAndAgerange(String argrang, String daomin, Pageable pageable);

	/**
	 * @Method Name : update
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 :
	 * @param account
	 */
	public void update(UserUpdateReqDto account);

	/**
	 * @Method Name : deleteById
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 :
	 * @param id
	 */
	public void deleteById(Long id);

	/**
	 * @Method Name : deleteBySnsid
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 :
	 * @param id
	 */
	public void deleteBySnsid(Long id);

	/**
	 * @Method Name : findById
	 * @작성일 : 2021. 7. 5.
	 * @작성자 : User1
	 * @변경이력 :
	 * @Method 설명 :
	 * @param id
	 * @return
	 */
	public User findById(Long id);

}
