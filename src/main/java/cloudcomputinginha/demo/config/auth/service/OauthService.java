package cloudcomputinginha.demo.config.auth.service;

import cloudcomputinginha.demo.config.auth.JwtProvider;
import cloudcomputinginha.demo.config.auth.domain.GoogleUser;
import cloudcomputinginha.demo.config.auth.dto.GuestLoginResponse;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.enums.SocialProvider;
import cloudcomputinginha.demo.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final List<SocialOauth> socialOauthList;
    private final HttpServletResponse httpServletResponse;
    private final GoogleUserInfoClient googleUserInfoClient;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Value("${frontend.redirect-uri}")
    private String frontendRedirectUrl;

    public void request(SocialProvider socialProvider) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialProvider);
        String redirectURL = socialOauth.getOauthRedirectURL();
        try {
            httpServletResponse.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void oauthLoginCallback(SocialProvider socialProvider, String code) {
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

        String accessToken = jwtProvider.generateAccessToken(member.getId(), false);
        String refreshToken = jwtProvider.generateRefreshToken(member.getId());

        member.setRefreshToken(refreshToken);
        memberRepository.save(member);

        String redirectUrl = frontendRedirectUrl + "?at=" + accessToken + "&rt=" + refreshToken;

        try {
            httpServletResponse.sendRedirect(redirectUrl);
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

    // 게스트 사용자
    public GuestLoginResponse loginAsGuest() {
        String name = "게스트" + new Random().nextInt(10000);
        Member guest = Member.createGuest(name);
        memberRepository.save(guest);

        String accessToken = jwtProvider.generateAccessToken(guest.getId(), true);

        return new GuestLoginResponse(accessToken, guest.getId(), guest.getName());
    }
}
