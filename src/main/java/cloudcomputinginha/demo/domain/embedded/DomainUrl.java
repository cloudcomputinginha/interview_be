package cloudcomputinginha.demo.domain.embedded;

import cloudcomputinginha.demo.apiPayload.code.handler.NotificationHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.config.properties.DomainProperties;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class DomainUrl {
    private String domainUrl;

    public DomainUrl(String domainUrl, DomainProperties domainProperties) {
        if (domainUrl == null || domainUrl.isBlank()) {
            throw new NotificationHandler(ErrorStatus.URL_INVALID);
        }
        if (!domainUrl.startsWith(domainProperties.getBackend()) && !domainUrl.startsWith(domainProperties.getAi())) { //BE 서버나 AI 서버 URL이 아니라면
            throw new NotificationHandler(ErrorStatus.URL_INVALID);
        }
        this.domainUrl = domainUrl;
    }
}
