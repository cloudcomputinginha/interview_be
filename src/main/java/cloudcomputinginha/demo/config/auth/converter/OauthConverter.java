package cloudcomputinginha.demo.config.auth.converter;

import cloudcomputinginha.demo.domain.enums.SocialProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class OauthConverter implements Converter<String, SocialProvider> {
    @Override
    public SocialProvider convert(String s) {
        return SocialProvider.valueOf(s.toUpperCase());
    }
}
