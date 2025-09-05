package com._oormthon.goEuro.member.controller;

import com._oormthon.goEuro.member.dto.ApiResponse;
import com._oormthon.goEuro.member.dto.KakaoLogin_code;
import com._oormthon.goEuro.member.service.MemberService;
import com._oormthon.goEuro.member.dto.AuthResponseDTO.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Kakao Auth", description = "카카오 로그인/로그아웃 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "카카오 로그인", description = "카카오 인가 코드를 이용한 로그인")
    @PostMapping("/kakao/login")
    public ApiResponse<LoginResponse> kakaoLogin(
            @RequestBody KakaoLogin_code request
    ) {
        return ApiResponse.onSuccess("카카오 로그인 성공", memberService.kakaoLogin(request.getCode()));
    }

    @Operation(summary = "카카오 로그아웃", description = "액세스 토큰으로 카카오 로그아웃")
    @PostMapping("/kakao/logout")
    public ResponseEntity<ApiResponse<Void>> kakaoLogout(
            @Parameter(description = "Bearer 액세스 토큰", required = true)
            @RequestHeader("Authorization") String authHeader
    ) {
        String accessToken = authHeader.replace("Bearer ", "").trim();
        memberService.kakaoLogout(accessToken);
        return ResponseEntity.ok(ApiResponse.onSuccess("카카오 로그아웃 성공", null));
    }
}
