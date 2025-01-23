package com.modernbank.customer.controller;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RestController
public class LoginController {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @GetMapping("/welcome")
    public ResponseEntity<Map<String, String>> welcome(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, String> response = new HashMap<>();
        
        System.out.println("여기까지는 오고 있음");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("여기까지는 오고 있음2222");
            try {
                if (validateToken(token)) {
                    response.put("status", "success");
                    response.put("message", "로그인에 성공했습니다!");
                    return ResponseEntity.ok(response);
                } else {
                    System.out.println("인벨리드 토큰");
                }
            } catch (Exception e) {
                return ResponseEntity.status(401)
                    .body(Map.of("status", "error", 
                                "message", "Invalid token"));
            }
        }
        
        return ResponseEntity.status(401)
            .body(Map.of("status", "error", 
                        "message", "Authentication required"));
    }

    @GetMapping("/login-failed")
    public ResponseEntity<Map<String, String>> loginFailed() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "로그인에 실패했습니다. 다시 시도해주세요.");
        return ResponseEntity.status(401).body(response);
    }

    private boolean validateToken(String token) {
        try {
            System.out.println("JWT Secret: " + jwtSecret);
            System.out.println("Validating token: " + token);
    
            byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
    
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(decodedKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    
            System.out.println("Successfully parsed claims: " + claims);
            
            Date expiration = claims.getExpiration();
            return expiration == null || expiration.after(new Date());
        } catch (Exception e) {
            System.out.println("Token validation error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}