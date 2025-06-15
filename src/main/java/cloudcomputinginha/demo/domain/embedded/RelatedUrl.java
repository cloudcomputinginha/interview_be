package cloudcomputinginha.demo.domain.embedded;

import cloudcomputinginha.demo.apiPayload.code.handler.NotificationHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RelatedUrl {
    private String url;

    public RelatedUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new NotificationHandler(ErrorStatus.URL_INVALID);
        }
        if (!url.startsWith("/")) {
            throw new NotificationHandler(ErrorStatus.URL_INVALID);
        }
        this.url = url;
    }
}
