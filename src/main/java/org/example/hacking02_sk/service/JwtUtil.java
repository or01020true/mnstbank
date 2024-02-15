package org.example.hacking02_sk.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.example.hacking02_sk.model.User;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class JwtUtil {
    private static final String SECRET_KEY = "sesac1MONEYST1team1secret1key1yek1terces1maet1TSYENOM1cases";
	public static final boolean JWT_MODE = false; // true = jwt auth, false = session auth 

    public String setToken(String userId, String userLevel, int time) {
		Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("userId", userId)
				.setIssuedAt(now)
				.setExpiration(new Date(System.currentTimeMillis() + (1000 * time)))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

	public boolean validateToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

	public String getToken(Cookie[] cookies) {
		String jwt = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT")) {
                    jwt = cookie.getValue();
					break;
                }
            }
        }
		return jwt;
 	}

	public boolean removeToken(Cookie[] cookies, HttpServletResponse response) {
		boolean result = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
				if(cookie.getName().equals("JWT")) {
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
					result = true;
					break;
				}
            }
        }
		return result;
	}

	private String getData(String jwt, String key) throws Exception {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
		return (String)claims.get(key);
	}

	public String extractUserId(String jwt) {
		String userId = null;
		try {
			userId = getData(jwt, "userId");
		} catch (Exception e) {
			e.printStackTrace();
 		}
		return userId;
	}

	public String extractUserLevel(String jwt) {
		String userLevel = null;
		try {
			userLevel = getData(jwt, "userLevel");
		} catch (Exception e) {
			e.printStackTrace();
 		}
		return userLevel;
	}
}
