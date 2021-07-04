package com.kakaoenterprise.web.controll;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.web.dto.UserDto;
import com.kakaoenterprise.web.dto.UserUpdateReqDto;
import com.kakaoenterprise.web.service.UserService;
import com.kakaoenterprise.web.service.UserServiceImpl;
import com.sun.istack.NotNull;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserServiceImpl userService;

	@ApiOperation(value = "전체 회원 검색", notes = "폐이징 및 정렬기능")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "agerange", dataType = "string", paramType = "query", value = "연령대 10~19"),
			@ApiImplicitParam(name = "domain", dataType = "string", paramType = "query", value = "이메일도메인 like %'kakaoenterprise.com'"),
			@ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "페이지당 수"),
			@ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "정렬기준 형식(,asc|desc).기본은 asc,여러 기준 정렬 가능") })
	@GetMapping("/api/v1/users")
	public ResponseEntity<Page<UserDto>> list(
			@RequestParam(value = "agerange", required = false, defaultValue = "") String agerange,
			@RequestParam(value = "domain", required = false, defaultValue = "") String domain,
			@NotNull final Pageable pageable) {
		Page<UserDto> posts = null;
		if ("".equals(domain) && "".equals(agerange)) {
			posts = userService.findAll(pageable);
		} else if ("".equals(domain)) {
			posts = userService.findbyAgerange(agerange, pageable);
		} else if ("".equals(agerange)) {
			posts = userService.findByEmailEndingWith(domain, pageable);
		} else {
			posts = userService.findByEmailEndingWithAndAgerange(domain, agerange, pageable);
		}
		return new ResponseEntity<>(posts, HttpStatus.OK);
	}

	@ApiOperation(value = "Id로 회줭정보 수정", notes = "로컬정만 변경")
	@PostMapping("/api/v1/user/{id}")
	public ResponseEntity<?> update(@PathVariable int id, @RequestBody UserUpdateReqDto userUpdateReqDto) {
		userService.update(userUpdateReqDto);
		return new ResponseEntity<>(1, HttpStatus.OK);
	}

	@ApiOperation(value = "Id로 회줭정보 삭제", notes = "카카오 가입자는 연결끊기까지 처리")
	@DeleteMapping("/api/v1/user/{id}")
	public ResponseEntity<?> deleteId(@PathVariable Long id) {
		User user = userService.findById(id);
		if (user.getSysid() != null) {
			// 카카오에 로그아웃
		}
		userService.deleteById(id);
		return new ResponseEntity<>(1, HttpStatus.OK);
	}

}
