package com.kakaoenterprise.web.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.web.dto.UserDto;
import com.kakaoenterprise.web.dto.UserUpdateReqDto;
import com.kakaoenterprise.web.repository.UserRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
	@Autowired
	private ApplicationContext context;

	@Mock
	private UserRepository userRepository;

	private UserServiceImpl userService;

	@BeforeEach
	public void beforeEach() {
		userService = new UserServiceImpl(userRepository);
	}

	@Test
	void findAllTest() {
		// given
		User member1 = User.builder().username("Tset").nickname("Tset").password("111").id(1L).build();
		Pageable pageable = PageRequest.of(0, 8);
		List<User> members = new ArrayList<>();
		members.add(member1);
		Page<User> pagedUser = new PageImpl(members);
		given(userRepository.findAll(pageable)).willReturn(pagedUser);
		// when
		Page<UserDto> findMembers = userService.findAll(pageable);
		List<UserDto> list = findMembers.getContent();
		System.out.println("findMembers = " + list);
		// then
		assertEquals(1, list.size());
		assertEquals(member1.getNickname(), list.get(0).getNickname());
	}

	@Test
	void findbyAgerange() {
		// given
		User member1 = User.builder().id(1L).nickname("Tset").sysid("Test").id(1L).agerange("10~19").build();
		Pageable pageable = PageRequest.of(0, 8);
		List<User> members = new ArrayList<>();
		members.add(member1);
		Page<User> pagedUser = new PageImpl(members);

		given(userRepository.findByAgerange("10~19", pageable)).willReturn(pagedUser);
		// when
		Page<UserDto> findMembers = userService.findbyAgerange("10~19", pageable);
		List<UserDto> list = findMembers.getContent();
		System.out.println("findMembers = " + list);
		// then
		assertEquals(1, list.size());
		assertEquals(member1.getNickname(), list.get(0).getNickname());
	}

	@Test
	void findByEmailEndingWith() {
		// given
		User member1 = User.builder().id(1L).nickname("Tset").sysid("Test").id(1L).agerange("10~19").email("a@a.com")
				.build();
		Pageable pageable = PageRequest.of(0, 8);
		List<User> members = new ArrayList<>();
		members.add(member1);
		Page<User> pagedUser = new PageImpl(members);
		given(userRepository.findByEmailEndingWith("a@a.com", pageable)).willReturn(pagedUser);
		// when
		Page<UserDto> findMembers = userService.findByEmailEndingWith("a@a.com", pageable);
		List<UserDto> list = findMembers.getContent();
		System.out.println("findMembers = " + list);
		// then
		assertEquals(1, list.size());
		assertEquals(member1.getNickname(), list.get(0).getNickname());
	}

	@Test
	void findByEmailEndingWithAndAgerange() {
		// given
		User member1 = User.builder().id(1L).nickname("Tset").sysid("Test").id(1L).agerange("10~19").email("a@a.com")
				.build();
		Pageable pageable = PageRequest.of(0, 8);
		List<User> members = new ArrayList<>();
		members.add(member1);
		Page<User> pagedUser = new PageImpl(members);
		given(userRepository.findByEmailEndingWithAndAgerange("10~19", "a@a.com", pageable)).willReturn(pagedUser);

		// when
		Page<UserDto> findMembers = userService.findByEmailEndingWithAndAgerange("10~19", "a@a.com", pageable);
		List<UserDto> list = findMembers.getContent();
		System.out.println("findMembers = " + list);
		// then
		assertEquals(1, list.size());
		assertEquals(member1.getNickname(), list.get(0).getNickname());
	}

	@Test
	public void findById() {
		// given
		User member1 = User.builder().id(1L).nickname("Tset").sysid("Test").id(1L).agerange("10~19").email("a@a.com")
				.build();
		Optional<User> ouser = Optional.of(member1);
		given(userRepository.findById(1L)).willReturn(ouser);
		// when
		User findMembers = userService.findById(1L);
		// then
		assertEquals(member1.getNickname(), findMembers.getNickname());
	}

	@Test
	public void update() {
		// given
		UserUpdateReqDto member1 = UserUpdateReqDto.builder().id(1L).nickname("Test").build();
		given(userRepository.update("Test", new Long(1))).willReturn(1);
		// when
		int numer = userService.update(member1);
		// then
		assertEquals(1, numer);
	}

	@Test
	public void deleteBySnsid() {
		// given
		UserUpdateReqDto member1 = UserUpdateReqDto.builder().id(1L).nickname("Test").build();
		given(userRepository.deleteBySnsid(1L)).willReturn(1);
		// when
		int numer = userService.deleteBySnsid(1L);
		// then
		assertEquals(1, numer);
	}

	@Test
	public void deleteById() {
		User person = User.builder().username("Tset").nickname("Tset").password("111").id(1L).build();
		org.junit.Assert.assertNull(userRepository.getById(person.getId()));
		userService.Save(person);
		userService.deleteById(person.getId());
		org.junit.Assert.assertNull(userService.findById(person.getId()));
	}

	@Test
	public void Save() {
		User person = User.builder().username("Tset").nickname("Tset").password("111").id(1L).build();
		org.junit.Assert.assertNull(userRepository.getById(person.getId()));
		userService.Save(person);
		org.junit.Assert.assertNull(userService.findById(person.getId()));
	}

}