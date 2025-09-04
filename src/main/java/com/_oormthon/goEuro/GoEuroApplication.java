package com._oormthon.goEuro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication                           // 컴포넌트 스캔 + 자동설정 활성화
public class GoEuroApplication {

	public static void main(String[] args) {     // 자바 진입점
		SpringApplication.run(GoEuroApplication.class, args); // 애플리케이션 실행
	}
}
