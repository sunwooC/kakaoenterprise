package com.kakaoenterprise.web.service.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import com.kakaoenterprise.domain.user.RoleType;
import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.web.dto.UserLoginDto;
import com.kakaoenterprise.web.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@SpringBootTest
public class WebLoginServicImplTest {
	
	@Mock
	UserLoginDto userLoginDto;
	
	@Test
	public void loadUserByUsernameTest() {
		MockitoAnnotations.initMocks(this);

		assertTrue(userLoginDto != null);
		//when(userLoginDto.getUsername()).thenReturn("username").thenReturn(true);
	}

	  
	@Test
	public void joinTest() {
		assertEquals("11", "11");
	}
	@Test
	public void mergeTest() {
		assertEquals("11", "11");
	}
}
