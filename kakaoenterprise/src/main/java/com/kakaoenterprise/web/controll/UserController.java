package com.kakaoenterprise.web.controll;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.web.dto.Message;
import com.kakaoenterprise.web.dto.UserDto;
import com.kakaoenterprise.web.dto.UserUpdateReqDto;
import com.kakaoenterprise.web.exception.ExceptionEnum;
import com.kakaoenterprise.web.service.impl.KakaoServiceImpl;
import com.kakaoenterprise.web.service.impl.RedisUserImpl;
import com.kakaoenterprise.web.service.impl.UserServiceImpl;
import com.sun.istack.NotNull;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.data.web.SortDefault;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.kakaoenterprise.web.exception.ApiException;
/**
 * local 및 카카오 사용자를 함께 조회 변경
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserServiceImpl userService;
	private final KakaoServiceImpl kakaoServiceImpl;
	private final RedisUserImpl redisUserImpl;

	/**
	 * @Method Name  : list
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : 조선우
	 * @변경이력  :
	 * @Method 설명 :
	 * @param agerange
	 * @param domain
	 * @param pageable
	 * @return
	 */
	@ApiOperation(value = "전체 회원 검색", notes = "폐이징 및 정렬기능")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "agerange", dataType = "string", paramType = "query", value = "연령대 10~19"),
			@ApiImplicitParam(name = "domain", dataType = "string", paramType = "query", value = "이메일도메인 like %'kakaoenterprise.com'"),
			@ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "페이지당 수"),
			@ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "정렬형식 agerange.asc-=email.desc 형태") })
	@GetMapping("/api/v1/users")
	public ResponseEntity<Page<UserDto>> list(
			@RequestParam(value = "agerange", required = false, defaultValue = "") String agerange,
			@RequestParam(value = "domain", required = false, defaultValue = "") String domain,
			@RequestParam(value = "page", required = false, defaultValue = "") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size,
			String sort){
		Page<UserDto> posts = null;
		Sort sortordery = Sort.by("id");
		sortordery = sortOrder(sortordery,sort);
		Pageable pageable = PageRequest.of(page, size,sortordery);
		
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
	private Sort sortOrder(Sort sort,String paramSort) {
		if(paramSort == null) {
			return sort;
		}
		String[] sorts = paramSort.split("-");
		if (sorts == null) {
			return sort;
		}
		sort = sort.unsorted();
		for(String item : sorts) {
			item = item.trim();
			String data[] = item.split(".");
			if(data.length == 0 ){
				continue;
			}
			if(data.length == 1 ){
				sort = sort.and(Sort.by(data[0]));
				continue;
			}
			sort =  sort.and(Sort.by(Sort.Direction.fromString(data[1]),data[0]));
		}
		return sort;
	}

	/**
	 * @Method Name  : update
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : 조선우
	 * @변경이력  :
	 * @Method 설명 : 사용자 정보중 닉네임을 변경하는 기능
	 * @param id
	 * @param userUpdateReqDto
	 * @return
	 */
	@ApiOperation(value = "로컬 Id로 회줭정보 수정", notes = "로컬정만 변경")
	@PostMapping("/api/v1/user/{id}")
	public ResponseEntity<?> update(@PathVariable int id
			, @RequestBody  @Valid UserUpdateReqDto userUpdateReqDto,BindingResult result) {
		if(result.hasErrors()){
			return new ResponseEntity<>(new Message(result.getAllErrors().get(0).getDefaultMessage()), HttpStatus.BAD_REQUEST);
		}	
		userService.update(userUpdateReqDto);
		return new ResponseEntity<>(new Message("update"), HttpStatus.OK);
	}

	/**
	 * @Method Name  : deleteId
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : 조선우
	 * @변경이력  :
	 * @Method 설명 : 사용자 정보를 삭제하는 기능 
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "로컬 Id로 회줭정보 삭제", notes = "카카오 가입자는 연결끊기까지 처리")
	@DeleteMapping("/api/v1/user/{id}")
	public ResponseEntity<?> deleteId(@PathVariable Long id) {
		User user = userService.findById(id);
		if ("Kakao".equals(user.getSysid())) {
			// 카카오에 로그아웃
			String snsid = user.getUsername().replace("Kakao_", "");
			ResponseEntity<String> result = kakaoServiceImpl.unlinkAdmin(snsid);
			if (result.getStatusCode() == HttpStatus.OK) {
				String sessionId = redisUserImpl.logout(user.getUsername());
				boolean logoutCheck = logout(sessionId);
			}
		}
		userService.deleteById(id);
		return new ResponseEntity<>(new Message("delete"), HttpStatus.OK);
	}
	/**
	 * @Method Name  : logout
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 :세션Id와 세션을 만료 시는 기능
	 * @param sessionId 세션의 ID
	 * @return 로그인 사용자 강제로그아웃 사용자가 같을때 true
	 */
	private boolean logout(String sessionId) {
		HttpSession session = getCurrentUserAccount(false);
		if (session != null && session.getId().equals(sessionId)) {
			session.removeAttribute("sessionId");
			session.removeAttribute("sessionUserName");
			session.invalidate(); //세션의 모든 속성을 삭제
			return true;
		}
		return false;
	}
	/**
	 * @Method Name  : getCurrentUserAccount
	 * @작성일   : 2021. 7. 5.
	 * @작성자   : User1
	 * @변경이력  :
	 * @Method 설명 : 세션정보를 반환
	 * @return sessionChk : 값에 따라 세션 생성 여부 결정
	 */
	public HttpSession getCurrentUserAccount(boolean sessionChk) {
		ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
		HttpSession httpSession = servletRequestAttribute.getRequest().getSession(sessionChk);
		return httpSession;
	}
}
