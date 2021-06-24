package com.kakaoenterprise.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.kakaoenterprise.service.KakaoOAuth2UserService;




@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private KakaoOAuth2UserService kakaoOAuth2UserService;

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.oauth2Login().userInfoEndpoint().userService(kakaoOAuth2UserService); 
    }
	
}