package dev.francode.ordersystem.service.auth;

import dev.francode.ordersystem.dto.auth.*;
import dev.francode.ordersystem.entity.UserApp;
import dev.francode.ordersystem.entity.enums.ERole;
import dev.francode.ordersystem.exceptions.custom.ValidationException;
import dev.francode.ordersystem.repository.UserRepository;
import dev.francode.ordersystem.security.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserResponse registerUser(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("El correo electrónico ya está registrado.");
        }

        UserApp user = new UserApp();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRol(ERole.valueOf(request.getRol().toUpperCase()));

        UserApp savedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .rol(savedUser.getRol().name())
                .build();
    }

    @Override
    public JwtResponse registerCustomer(CustomerRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("El correo electrónico ya está registrado.");
        }

        UserApp user = new UserApp();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRol(ERole.CLIENTE);

        UserApp savedUser = userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);

        return JwtResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .rol(savedUser.getRol().name())
                .build();
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (Exception e) {
            throw new ValidationException("Credenciales inválidas. Por favor, verifique su correo y contraseña.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return JwtResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .rol(userDetails.getAuthorities().stream().findFirst().get().getAuthority())
                .build();
    }

    @Override
public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
    String refreshToken = request.getRefreshToken();

    try {
        jwtUtils.validateJwtToken(refreshToken);
    } catch (ExpiredJwtException e) {
        throw new ValidationException("Refresh token expirado. Por favor, inicie sesión nuevamente.");
    } catch (JwtException e) {
        throw new ValidationException("Refresh token inválido.");
    }

    String email = jwtUtils.getUserNameFromJwtToken(refreshToken);
    Claims claims = jwtUtils.getAllClaimsFromToken(refreshToken);
    String role = claims.get("role", String.class);

    // Usamos método que no requiere Authentication
    String newAccessToken = jwtUtils.generateTokenFromEmail(email, role);

    return RefreshTokenResponse.builder()
            .token(newAccessToken)
            .build();
}

}
