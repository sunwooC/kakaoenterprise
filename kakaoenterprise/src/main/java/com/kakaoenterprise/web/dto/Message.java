package com.kakaoenterprise.web.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Message {
	@ApiParam(value = "요청 처리에 대한 내용", required = false, example = "login")
	String message; 
}
