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

	@Query(value = "UPDATE User u SET u.nickname = :nickname WHERE u.id = :id", nativeQuery = false)
	@Modifying
	public Integer update(String nickname, Long id);

	@Modifying
	@Query("delete from User b where b.snsid=:snsid")
	int deleteBySnsid(@Param("snsid") Long snsid);

	public Page<User> findByAgerange(String arerang, Pageable pageable);

	public Page<User> findByEmailEndingWith(String email, Pageable pageable);

	public Page<User> findByEmailEndingWithAndAgerange(String daomin, String arerang, Pageable pageable);

}
