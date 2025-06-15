package cloudcomputinginha.demo.config.auth;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();
                                configuration.setAllowedOrigins(List.of(
                                        "http://localhost:3000",
                                    "https://cloud-computing-fe-two.vercel.app",
                                    "http://127.0.0.1:5500"
                                ));

                                configuration.setAllowedMethods(
                                        List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                                );
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);
                                configuration.setAllowCredentials(true);
                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                                return configuration;
                            }
                        })
                )
                //csrf disable
                .csrf(AbstractHttpConfigurer::disable)

                //form login 방식 disable
                .formLogin(AbstractHttpConfigurer::disable)

                //http basic 인증 방식 disable
                .httpBasic(AbstractHttpConfigurer::disable)

                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)


                //경로별 인가 작업
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/auth/google").permitAll()
                        .requestMatchers("/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-resources/**"
//                                "/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
        ;

         return httpSecurity.build();

    }



}
