package com.kakaoenterprise.web.controll;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kakaoenterprise.domain.user.User;
import com.kakaoenterprise.web.dto.AuthJoinReqDto;
import com.kakaoenterprise.web.dto.UserDto;
import com.kakaoenterprise.web.dto.UserLoginDto;
import com.kakaoenterprise.web.exception.ExceptionEnum;
import com.kakaoenterprise.web.repository.UserRepository;
import com.kakaoenterprise.web.service.WebLoginServicImpl;
import com.kakaoenterprise.web.exception.ApiException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class WebLoginController {
	private final WebLoginServicImpl userImpl;

	@RequestMapping("/api/v1/auth/local")
	public ResponseEntity login(@RequestBody UserLoginDto user){
		User info = userImpl.loadUserByUsername(user);
		if (info == null) {
			throw new ApiException(ExceptionEnum.NOT_FOUND_USER);
		}
		String password = ecodeing(user.getPassword());
		if(!password.equals(info.getPassword())) {
			throw new ApiException(ExceptionEnum.NOT_MATCHED_USER);
		}
		// return "/user/userlist";
		HttpSession session = getCurrentUserAccount();
		session.setAttribute("userInfo", info);
		
		return new ResponseEntity<>(1, HttpStatus.OK);
	}

	
	@RequestMapping("/login/join")
	public ResponseEntity join(@RequestBody AuthJoinReqDto authJoinReqDto) {
		
		authJoinReqDto.setPassword(ecodeing(authJoinReqDto.getPassword()));
		boolean result = userImpl.join(authJoinReqDto.toEntity());
		if(result != true) {
			throw new ApiException(ExceptionEnum.NOT_HAS_USER);
		}
		return new ResponseEntity<>(1, HttpStatus.OK);
	}

	
	
	/*
	 * @RequestMapping("/user/userlist") public String userList() { return
	 * "/user/userlist"; }
	 */
	public String ecodeing(String password) {
		String ecodeing = "";
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			ecodeing = String.format("%064x", new BigInteger(1, md.digest()));
		}catch (Exception e) {
		}
		return ecodeing;
	}
	public HttpSession getCurrentUserAccount() {
	    ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession httpSession = servletRequestAttribute.getRequest().getSession(true);
	    return httpSession;
	}
	

}
