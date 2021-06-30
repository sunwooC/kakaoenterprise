package com.kakaoenterprise;

import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UserContoller {
	@GetMapping("/login/oauth2/code/kakao")
	public HashMap callback(String code) {
		HashMap map = new HashMap();
		map.put("code", code);
		
		
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "baea2cf9d9e01dd7788f24fb1626cf3d");
		params.add("redirect_uri","http://localhost:8080/login/oauth2/code/kakao");
		params.add("code",code);
		HttpEntity<MultiValueMap<String,String>> req = new HttpEntity<>(params,headers);
		ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token",HttpMethod.POST,req,String.class);
		System.out.println(response);
		
		
		
		return map;
	}
}
