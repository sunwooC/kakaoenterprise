package com.kakaoenterprise.domain.user;


import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import com.kakaoenterprise.web.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class User implements Serializable{
	static final long serialVersionUID = 4136285672735957787L;
	@Id // Primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 시퀀스, auto_increment

	@Column(nullable = false, length = 100, unique = true)
	private String username;

	@Column(nullable = true, length = 256) // 123456 => 해쉬 (비밀번호 암호화)
	private String password;

	@Column(nullable = false, length = 50)
	private String email;

	@Column(nullable = false, length = 100)
	private String nickname; //사용자가 변경가능한 부분

	@Column(nullable = true)
	private Long snsid; //

	@Column(nullable = true, length = 100)
	private String sysid; //

	@Enumerated(EnumType.STRING)
	private RoleType role; // ADMIN, USER

	@Column(nullable = true, length = 6)
	private String agerange; //

	@Column(nullable = true)
	private Integer agegrop; //
	
	@Column(nullable = true, length = 60)
	private String refreshToken; //
	
	@Column(nullable = true, length = 60)
	private String accessToekn; //


	
	@CreationTimestamp
	private Timestamp createDate;

	public UserDto convertToAccountResDto() {
		return UserDto.builder()
				.nickname(this.nickname)
				.email(this.email)
				.id(this.id)
				.agerange(this.agerange)
				.agegrop(agegrop)
				.refreshToken(refreshToken)
				.accessToekn(accessToekn)
				.sysid(sysid)
				.build();
	}


}
