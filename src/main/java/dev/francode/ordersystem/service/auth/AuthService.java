package dev.francode.ordersystem.service.auth;

import dev.francode.ordersystem.dto.auth.*;

public interface AuthService {
    UserResponse registerUser(UserRegisterRequest userRegisterRequest);
    JwtResponse authenticateUser(LoginRequest loginRequest);
    JwtResponse registerCustomer(CustomerRegisterRequest customerRegisterRequest);
    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
