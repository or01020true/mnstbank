package org.example.hacking02_sk.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {
    private static final String SECRET_KEY = "sesac1MONEYST1team1secret1key1yek1terces1maet1TSYENOM1cases";
    //private static final ;

    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1800000)) // 30분 유효 기간
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /*
		public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
			final Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
			return claimsResolver.apply(claims);
		}

		public static String extractUserId(String token) {
			return extractClaim(token, Claims::getSubject);
		}

		public boolean validateToken(String token) {
			try {
				Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		*/
}
