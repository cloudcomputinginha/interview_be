package cloudcomputinginha.demo.config.auth.controller;

import cloudcomputinginha.demo.config.auth.service.OauthService;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.enums.SocialProvider;
import cloudcomputinginha.demo.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Slf4j
public class OauthController {
    private final OauthService oauthService;
    private final MemberRepository memberRepository;

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
}
