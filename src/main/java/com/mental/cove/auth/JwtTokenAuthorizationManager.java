package com.mental.cove.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Supplier;

@Slf4j
@Component
public class JwtTokenAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    public static final String AUTH_BEARER = "Bearer ";

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        HttpServletRequest request = object.getRequest();
        String token = request.getHeader("Authorization");
        token = StringUtils.substringAfter(token, AUTH_BEARER);
        if(StringUtils.isBlank(token)) {
            return new AuthorizationDecision(false);
        }
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            String accessToken = claims.getPayload().get("access_token", String.class);
            Date expiration = claims.getPayload().getExpiration();
            String openid = claims.getPayload().getSubject();
            JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(accessToken, openid, null);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
            if (accessToken != null && !expiration.before(new Date())) {
                return new AuthorizationDecision(true);
            } else {
                return new AuthorizationDecision(false);
            }
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature", ex);
            return new AuthorizationDecision(false);
        } catch (ExpiredJwtException ex) {
            log.error("JWT token is expired", ex);
            return new AuthorizationDecision(false);
        } catch (Exception ex) {
            log.error("Invalid JWT token", ex);
            return new AuthorizationDecision(false);
        }
    }
}