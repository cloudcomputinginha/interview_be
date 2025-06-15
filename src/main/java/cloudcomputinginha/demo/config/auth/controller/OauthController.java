package cloudcomputinginha.demo.config.auth.controller;

import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.config.auth.JwtProvider;
import cloudcomputinginha.demo.config.auth.dto.TokenReissueRequestDto;
import cloudcomputinginha.demo.config.auth.dto.TokenReissueResponseDto;
import cloudcomputinginha.demo.config.auth.service.OauthService;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.enums.SocialProvider;
import cloudcomputinginha.demo.repository.MemberRepository;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Slf4j
public class OauthController {
    private final OauthService oauthService;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @GetMapping(value = "/{socialProvider}")
    @Operation(summary = "소셜 로그인 시작 API", description = "소셜 로그인 시작합니다.")
    public void socialProvider(@PathVariable(name = "socialProvider")SocialProvider socialProvider) {
        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialProvider);
        oauthService.request(socialProvider);
    }

    @GetMapping(value = "/{socialProvider}/callback")
    @Operation(summary = "소셜 로그인 콜백 API", description = "소셜 로그인 콜백 처리합니다.")
    public void callback(@PathVariable(name = "socialProvider")SocialProvider socialProvider, @RequestParam(name = "code") String code) {
        log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);

        oauthService.oauthLoginCallback(socialProvider, code);
    }

    @DeleteMapping(value = "/logout")
    @Operation(summary = "소셜 로그아웃 API", description = "소셜 로그아웃 합니다.")
    public ResponseEntity<?> logout(@AuthenticationPrincipal Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        member.setRefreshToken(null);
        memberRepository.save(member);

        return ResponseEntity.ok("성공적으로 로그아웃 되었습니다.");
    }

    @PostMapping(value = "/reissue")
    @Operation(summary = "토큰 재발급 API", description = "refresh token을 통해 새로운 access token을 발급 받습니다.")
    public ApiResponse<TokenReissueResponseDto> reissueToken(@RequestBody TokenReissueRequestDto tokenReissueRequestDto) {
        String oldRefreshToken = tokenReissueRequestDto.getRefreshToken();

        if (!jwtProvider.validateToken(oldRefreshToken)) {
            throw new JwtException("유효하지 않은 refresh token 입니다.");
        }

        Long memberId = jwtProvider.getMemberIdFromToken(oldRefreshToken);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new JwtException("존재하지 않는 사용자입니다."));

        Map<String, String> tokens = jwtProvider.reissueTokens(oldRefreshToken, member);
        memberRepository.save(member);

        TokenReissueResponseDto responseDto = new TokenReissueResponseDto(
                tokens.get("accessToken"),
                tokens.get("refreshToken")
        );

        return ApiResponse.onSuccess(responseDto);

    }
}
