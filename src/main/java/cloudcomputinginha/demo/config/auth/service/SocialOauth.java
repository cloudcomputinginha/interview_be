package cloudcomputinginha.demo.config.auth.service;

import cloudcomputinginha.demo.domain.enums.SocialProvider;

public interface SocialOauth {
    String getOauthRedirectURL();
    String requestAccessToken(String code);

    default SocialProvider provider() {
        if (this instanceof GoogleOauth) {
            return SocialProvider.GOOGLE;
        } else {
            return null;
        }
    }
}
