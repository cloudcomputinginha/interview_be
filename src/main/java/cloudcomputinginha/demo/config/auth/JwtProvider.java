package cloudcomputinginha.demo.config.auth;

import cloudcomputinginha.demo.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60;       // 1시간
    private final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 14; // 2주

    public String generateAccessToken(Long memberId, boolean isGuest) {
        Date now = new Date();
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("isGuest", isGuest)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long memberId) {
        Date now = new Date();
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getMemberIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public Map<String, String> reissueTokens(String oldRefreshToken, Member member) {
        if (member.isGuest()) {
            throw new JwtException("게스트는 토큰 재발급이 불가능합니다.");
        }
        if (!validateToken(oldRefreshToken)) {
            throw new JwtException("유효하지 않은 refresh token 입니다.");
        }
        if (!oldRefreshToken.equals(member.getRefreshToken())) {
            throw new JwtException("저장된 refresh token과 일치하지 않습니다.");
        }

        String newAccessToken = generateAccessToken(member.getId(), false);
        String newRefreshToken = generateRefreshToken(member.getId());

        member.setRefreshToken(newRefreshToken);

        return Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken
        );
    }
}
