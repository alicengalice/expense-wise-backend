package com.expensewise.config;

import com.expensewise.controller.CategoryController;
import com.expensewise.service.CategoryService;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Checks that the backend accepts only tokens shaped like the ones the
 * Go auth service issues (HS256, shared secret, matching iss/aud, not expired).
 */
@WebMvcTest(controllers = CategoryController.class)
@Import({SecurityConfig.class, CorsConfig.class})
@TestPropertySource(properties = {
    "auth.jwt.secret=" + SecurityConfigTest.SECRET,
    "auth.jwt.issuer=" + SecurityConfigTest.ISSUER,
    "auth.jwt.audience=" + SecurityConfigTest.AUDIENCE
})
class SecurityConfigTest {

    static final String SECRET = "0123456789abcdef0123456789abcdef";
    static final String ISSUER = "test-issuer";
    static final String AUDIENCE = "test-audience";
    private static final String URL = "/api/categories";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    void stubService() {
        given(categoryService.getAllPaginated(any(Pageable.class))).willReturn(Page.empty());
    }

    // Same claims as go-auth-service/utils/jwt.go GenerateToken (aud as a single string)
    private static String token(String secret, String issuer, String audience, Instant expiresAt) throws Exception {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
            .subject("admin")
            .claim("username", "admin")
            .issuer(issuer)
            .audience(audience)
            .issueTime(new Date())
            .expirationTime(Date.from(expiresAt))
            .build();
        SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        jwt.sign(new MACSigner(secret.getBytes(StandardCharsets.UTF_8)));
        return jwt.serialize();
    }

    private static Instant inOneHour() {
        return Instant.now().plus(1, ChronoUnit.HOURS);
    }

    @Test
    void givenNoToken_whenGet_then401() throws Exception {
        mockMvc.perform(get(URL)).andExpect(status().isUnauthorized());
    }

    @Test
    void givenValidToken_whenGet_then200() throws Exception {
        mockMvc.perform(get(URL).header("Authorization", "Bearer " + token(SECRET, ISSUER, AUDIENCE, inOneHour())))
            .andExpect(status().isOk());
    }

    @Test
    void givenWrongSecret_whenGet_then401() throws Exception {
        String t = token("ffffffffffffffffffffffffffffffff", ISSUER, AUDIENCE, inOneHour());
        mockMvc.perform(get(URL).header("Authorization", "Bearer " + t)).andExpect(status().isUnauthorized());
    }

    @Test
    void givenExpiredToken_whenGet_then401() throws Exception {
        // Older than Spring's default 60s clock-skew allowance
        String t = token(SECRET, ISSUER, AUDIENCE, Instant.now().minus(5, ChronoUnit.MINUTES));
        mockMvc.perform(get(URL).header("Authorization", "Bearer " + t)).andExpect(status().isUnauthorized());
    }

    @Test
    void givenWrongIssuer_whenGet_then401() throws Exception {
        String t = token(SECRET, "someone-else", AUDIENCE, inOneHour());
        mockMvc.perform(get(URL).header("Authorization", "Bearer " + t)).andExpect(status().isUnauthorized());
    }

    @Test
    void givenWrongAudience_whenGet_then401() throws Exception {
        String t = token(SECRET, ISSUER, "other-api", inOneHour());
        mockMvc.perform(get(URL).header("Authorization", "Bearer " + t)).andExpect(status().isUnauthorized());
    }

    @Test
    void givenUnsignedAlgNoneToken_whenGet_then401() throws Exception {
        String t = new PlainJWT(new JWTClaimsSet.Builder()
            .subject("admin").issuer(ISSUER).audience(AUDIENCE)
            .expirationTime(Date.from(inOneHour())).build()).serialize();
        mockMvc.perform(get(URL).header("Authorization", "Bearer " + t)).andExpect(status().isUnauthorized());
    }

    @Test
    void givenPreflightRequest_whenOptions_thenNotUnauthorized() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options(URL)
                .header("Origin", "http://localhost:5173")
                .header("Access-Control-Request-Method", "GET"))
            .andExpect(status().isOk());
    }
}
