package com.example.aleksandr_rozkov_6020_pz2.security;

import com.example.aleksandr_rozkov_6020_pz2.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JwtService {
    private static final String SECRET = "it355-manul-blog-secret-key-for-simple-project";
    private static final ObjectMapper mapper = new ObjectMapper();

    public String generateToken(User user) {
        try {
            Map<String, Object> header = new LinkedHashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("sub", user.getEmail());
            payload.put("id", user.getId());
            payload.put("role", user.getRole());
            payload.put("exp", Instant.now().plusSeconds(60 * 60 * 24).getEpochSecond());

            String h = encode(mapper.writeValueAsBytes(header));
            String p = encode(mapper.writeValueAsBytes(payload));
            String signature = sign(h + "." + p);
            return h + "." + p + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException("Token generation failed");
        }
    }

    public String extractEmail(String token) {
        try {
            if (!isValid(token)) return null;
            String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);
            JsonNode node = mapper.readTree(payload);
            return node.get("sub").asText();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isValid(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length != 3) return false;
        String expected = sign(parts[0] + "." + parts[1]);
        if (!expected.equals(parts[2])) return false;
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        JsonNode node = mapper.readTree(payload);
        return node.get("exp").asLong() > Instant.now().getEpochSecond();
    }

    private static String encode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String sign(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return encode(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }
}
