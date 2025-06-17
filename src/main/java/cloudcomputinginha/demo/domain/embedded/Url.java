package cloudcomputinginha.demo.domain.embedded;

import cloudcomputinginha.demo.apiPayload.code.handler.NotificationHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.config.DomainInfo;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Url {
    private String url;

    public Url(String url) {
        if (url == null || url.isBlank()) {
            throw new NotificationHandler(ErrorStatus.URL_INVALID);
        }
        if (!url.startsWith(DomainInfo.INTERVIEW) && !url.startsWith(DomainInfo.INTERVIEW_AI)) { //BE 서버나 AI 서버 URL이 아니라면
            throw new NotificationHandler(ErrorStatus.URL_INVALID);
        }
        this.url = url;
    }
}
