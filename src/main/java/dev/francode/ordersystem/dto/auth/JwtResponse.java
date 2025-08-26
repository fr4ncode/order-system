package dev.francode.ordersystem.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    private String refreshToken;
    private Long id;
    private String email;
    private String rol;
}
