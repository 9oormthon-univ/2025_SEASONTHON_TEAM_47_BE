package com._oormthon.goEuro.member.service;

import com._oormthon.goEuro.member.dto.AuthResponseDTO;
import com._oormthon.goEuro.member.dto.AuthResponseDTO.LoginResponse;
import com._oormthon.goEuro.member.entity.Member;
import com._oormthon.goEuro.member.provider.KakaoAuthProvider;
import com._oormthon.goEuro.member.repository.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@RequiredArgsConstructor
public class MemberService {

    private final KakaoAuthProvider kakaoAuthProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public LoginResponse kakaoLogin(String code) {
        AuthResponseDTO.LoginResponse kakaoUser = kakaoAuthProvider.loginWithKakaoCode(code);

        Member member = memberRepository.findByEmail(kakaoUser.getEmail())
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .email(kakaoUser.getEmail())
                        .name(kakaoUser.getName())
                        .build())
                );

        return LoginResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .token(kakaoUser.getToken())
                .build();
    }

    public void kakaoLogout(String accessToken) {
        kakaoAuthProvider.logoutWithKakaoToken(accessToken);
    }
}