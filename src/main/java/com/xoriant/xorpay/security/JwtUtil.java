package com.xoriant.xorpay.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
 
@Service
public class JwtUtil {
	private String SECRET_KEY = "secret";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
    	try {
    		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    	}catch(Exception exception) {
    		throw new ExpiredJwtException(null, null,"Invalid token. Xor Token Expired. Please re-login");
    	}
    }

    private Boolean isTokenExpired(String token) {
    	
    	try {
    		return extractExpiration(token).before(new Date());
    	}catch(Exception exception) {
    		throw new ExpiredJwtException(null, null,null,new Exception("Xor Token Expired. Please re-login"));
    	}
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
//        return (username.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token));
        
        if((!username.equalsIgnoreCase(userDetails.getUsername())) || (isTokenExpired(token))) {
        	throw new BadCredentialsException("Xorpay INVALID_CREDENTIALS", new Exception("Please login to xorpay portal"));
        }
        return true;
    }
}
