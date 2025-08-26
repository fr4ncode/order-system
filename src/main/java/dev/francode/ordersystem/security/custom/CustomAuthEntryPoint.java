package dev.francode.ordersystem.security.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
        
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        Map<String, Object> body = new HashMap<>();
        
        Throwable cause = authException.getCause();
        if (cause != null && cause instanceof ExpiredJwtException) {
            body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            body.put("message", "Token expirado. Por favor, inicie sesión nuevamente.");
            body.put("error", "ERR_401_TOKEN_EXPIRED");
        } else if (request.getAttribute("expired") != null) {
            body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            body.put("message", "Token expirado. Por favor, inicie sesión nuevamente.");
            body.put("error", "ERR_401_TOKEN_EXPIRED");
        } else {
            body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            body.put("message", "No autenticado. Por favor, inicie sesión para acceder a este recurso.");
            body.put("error", "ERR_401_UNAUTHORIZED");
        }

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}
