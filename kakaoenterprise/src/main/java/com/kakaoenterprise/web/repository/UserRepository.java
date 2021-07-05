package com.kakaoenterprise.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kakaoenterprise.domain.user.User;

/**
 * 사용자 정보를 생성,변경, 삭제하는 기능
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
public interface UserRepository extends JpaRepository<User, Long> {

	public User findByUsername(String username);

	/**
	 * @Method Name  : update
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 :
	 * @param nickname
	 * @param id
	 * @return
	 */
	@Query(value = "UPDATE User u SET u.nickname = :nickname WHERE u.id = :id", nativeQuery = false)
	@Modifying
	public int update(String nickname, Long id);

	/**
	 * @Method Name  : deleteBySnsid
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 :
	 * @param snsid
	 * @return
	 */
	@Modifying
	@Query("delete from User b where b.snsid=:snsid")
	int deleteBySnsid(@Param("snsid") Long snsid);

	/**
	 * @Method Name  : findByAgerange
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 :
	 * @param arerang
	 * @param pageable
	 * @return
	 */
	public Page<User> findByAgerange(String arerang, Pageable pageable);

	/**
	 * @Method Name  : findByEmailEndingWith
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 :
	 * @param email
	 * @param pageable
	 * @return
	 */
	public Page<User> findByEmailEndingWith(String email, Pageable pageable);

	/**
	 * @Method Name  : findByEmailEndingWithAndAgerange
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 :
	 * @param daomin
	 * @param arerang
	 * @param pageable
	 * @return
	 */
	public Page<User> findByEmailEndingWithAndAgerange(String arerang, String daomin, Pageable pageable);

}
