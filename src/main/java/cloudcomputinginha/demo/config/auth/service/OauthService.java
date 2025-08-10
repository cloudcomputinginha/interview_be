package cloudcomputinginha.demo.config.auth.service;

import cloudcomputinginha.demo.config.auth.JwtProvider;
import cloudcomputinginha.demo.config.auth.domain.GoogleUser;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.enums.SocialProvider;
import cloudcomputinginha.demo.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final List<SocialOauth> socialOauthList;
    private final HttpServletResponse httpServletResponse;
    private final GoogleUserInfoClient googleUserInfoClient;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public void request(SocialProvider socialProvider) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialProvider);
        String redirectURL = socialOauth.getOauthRedirectURL();
        try {
            httpServletResponse.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void oauthLoginCallback(SocialProvider socialProvider, String code, String target, HttpServletResponse response) throws IOException {
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

        String redirectUrl = UriComponentsBuilder.fromUriString(target)
                .queryParam("at", accessToken)   // 프론트와 합의된 파라미터 키
                .queryParam("rt", refreshToken)
                .build(true)
                .toUriString();

        if (!response.isCommitted()) {
            response.sendRedirect(redirectUrl);
        }
    }

    private SocialOauth findSocialOauthByType(SocialProvider socialProvider) {
        return socialOauthList.stream()
                .filter(x -> x.provider() == socialProvider)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }
}
