package cloudcomputinginha.demo.config.auth.service;

import cloudcomputinginha.demo.config.auth.JwtProvider;
import cloudcomputinginha.demo.config.auth.domain.GoogleUser;
import cloudcomputinginha.demo.config.properties.FrontendRedirectProperties;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.enums.SocialProvider;
import cloudcomputinginha.demo.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final List<SocialOauth> socialOauthList;
    private final GoogleUserInfoClient googleUserInfoClient;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final FrontendRedirectProperties redirectProperties;

    private final ConcurrentMap<String, String> stateStore = new ConcurrentHashMap<>(); // TODO: Redis로 변경 필요

    public void request(SocialProvider socialProvider, String redirect, HttpServletResponse response) throws IOException {
        String finalRedirect = resolveAndValidateRedirect(redirect); // 화이트리스트 검증
        String state = UUID.randomUUID().toString();
        stateStore.put(state, finalRedirect); // 간단 저장 (TTL 미구현 데모)

        SocialOauth socialOauth = this.findSocialOauthByType(socialProvider);
        String redirectURL = socialOauth.getOauthRedirectURL(state);
        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void oauthLoginCallback(SocialProvider socialProvider, String code, String state, HttpServletResponse response) throws IOException {
        SocialOauth socialOauth = this.findSocialOauthByType(socialProvider);
        String googleAccessToken = socialOauth.requestAccessToken(code);

        GoogleUser userInfo = googleUserInfoClient.getUserInfo(googleAccessToken);

        Member member = memberRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .email(userInfo.getEmail())
                            .name(userInfo.getName())
                            .providerId(userInfo.getId())
                            .socialProvider(socialProvider)
                            .build();
                    return memberRepository.save(newMember);
                });

        String accessToken = jwtProvider.generateAccessToken(member.getId());
        String refreshToken = jwtProvider.generateRefreshToken(member.getId());

        member.setRefreshToken(refreshToken);
        memberRepository.save(member);

        String targetUrl = Optional.ofNullable(state)
                // state가 null이 아니므로, Optional.ofNullable은 값을 포함한 Optional을 반환합니다.
                // flatMap을 사용하여 stateStore에서 값을 가져옵니다.
                .flatMap(key -> Optional.ofNullable(stateStore.remove(key)))
                // 만약 stateStore에 해당 key가 존재하지 않아 remove()가 null을 반환하면,
                // flatMap의 결과는 Optional.empty()가 되어 orElse()가 실행됩니다.
                .orElse(redirectProperties.getDefaultRedirectUri());

        String redirectUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("at", accessToken)
                .queryParam("rt", refreshToken)
                .build(true)
                .toUriString();

        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            throw new RuntimeException("프론트엔드 리다이렉트 실패", e);
        }
    }

    private SocialOauth findSocialOauthByType(SocialProvider socialProvider) {
        return socialOauthList.stream()
                .filter(x -> x.provider() == socialProvider)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }

    private String resolveAndValidateRedirect(String redirect) {
        String candidate = (redirect == null || redirect.isBlank())
                ? redirectProperties.getDefaultRedirectUri()
                : redirect;
        List<String> allowedRedirectUris = redirectProperties.getAllowedRedirectUris();
        if (allowedRedirectUris == null || !allowedRedirectUris.contains(candidate)) {
            throw new IllegalArgumentException("허용되지 않은 redirect URI입니다.");
        }
        return candidate;
    }
}
