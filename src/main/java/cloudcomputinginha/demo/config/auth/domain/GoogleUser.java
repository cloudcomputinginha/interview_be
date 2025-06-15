package cloudcomputinginha.demo.config.auth.domain;

import lombok.Data;

@Data
public class GoogleUser {
    private String id;
    private String email;
    private String name;
    private String picture;
}
