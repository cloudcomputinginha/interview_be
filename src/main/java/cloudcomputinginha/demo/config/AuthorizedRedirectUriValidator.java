package cloudcomputinginha.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Component
public class AuthorizedRedirectUriValidator {
    private List<String> authorizedRedirectUris;

    public AuthorizedRedirectUriValidator(Environment env) {
        this.authorizedRedirectUris = Binder.get(env)
                .bind("app.oauth2.authorized-redirect-uris", Bindable.listOf(String.class))
                .orElse(List.of()); // 없으면 빈 리스트
        System.out.println("[auth-redirect] loaded uris = " + authorizedRedirectUris);
    }

    public boolean isAuthorized(String candidate) {
        try {
            URI c = URI.create(candidate);
            for (String allowed : authorizedRedirectUris) {
                URI a = URI.create(allowed);
                if (equalsSchemeHostPort(a, c) && pathStartsWith(a, c)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean equalsSchemeHostPort(URI a, URI b) {
        boolean scheme = a.getScheme().equalsIgnoreCase(b.getScheme());
        boolean host = a.getHost().equalsIgnoreCase(b.getHost());
        int ap = a.getPort() == -1 ? defaultPort(a.getScheme()) : a.getPort();
        int bp = b.getPort() == -1 ? defaultPort(b.getScheme()) : b.getPort();
        return scheme && host && ap == bp;
    }

    private boolean pathStartsWith(URI a, URI b) {
        String ap = a.getPath() == null ? "" : a.getPath();
        String bp = b.getPath() == null ? "" : b.getPath();
        return bp.startsWith(ap);
    }

    private int defaultPort(String scheme) { return "https".equalsIgnoreCase(scheme) ? 443 : 80; }
}
