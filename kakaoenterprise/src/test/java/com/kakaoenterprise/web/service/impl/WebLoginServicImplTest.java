package com.kakaoenterprise.web.service.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kakaoenterprise.domain.user.RoleType;
import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.web.dto.UserDto;
import com.kakaoenterprise.web.dto.UserLoginDto;
import com.kakaoenterprise.web.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class WebLoginServicImplTest {
	
	@Mock
	private UserRepository userRepository;

	private WebLoginServicImpl webLoginServicImpl;
	@BeforeEach
	public void beforeEach() {
		webLoginServicImpl = new WebLoginServicImpl(userRepository);
	}
	@Test
	public void loadUserByUsernameTest() {		
		// given
		User member1 = User.builder().username("Tset").nickname("Tset").password("111").id(1L).build();

		given(userRepository.findByUsername(member1.getUsername())).willReturn(member1);
		// when
		UserLoginDto user = UserLoginDto.builder().username("Tset").password("111").build();
		
		User retMeber = webLoginServicImpl.loadUserByUsername(user);
		assertEquals(member1.getUsername(),retMeber.getUsername());
	}
	@Test
	public void joinTest() {
		// given
		User member1 = User.builder().username("Tset").nickname("Tset").password("111").id(1L).build();
		given(userRepository.save(member1)).willReturn(member1);

		User retMeber = webLoginServicImpl.join(member1);
		assertEquals(member1.getUsername(),retMeber.getUsername());
	}
	@Test
	public void mergeTest() {
		// given
		User member1 = User.builder().username("Tset").nickname("Tset").password("111").id(1L).build();
		given(userRepository.save(member1)).willReturn(member1);
		// when
		
		User retMeber =  webLoginServicImpl.merge(member1);
		
		assertEquals(member1.getUsername(),retMeber.getUsername());
	}
}
