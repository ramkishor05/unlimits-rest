package org.unlimits.rest.token;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class TokenUtil{
	
	private static String BEARER="Bearer ";
	
	private static String USER_ROLE="role";
	
	private static String USER_ID="userId";

	private static final Logger log = LoggerFactory.getLogger(TokenUtil.class);

	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

	public static String createToken(Map<String, Object> claims, String userName,Long userId, String role) {
		log.debug("TokenServiceImpl :: createToken() started");
		return Jwts.builder().setClaims(claims).setSubject(userName)
				.setHeaderParam(USER_ROLE, role)
				.setHeaderParam(USER_ID, userId)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(buildExprireationDate())
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	private static Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private static Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	private static JwsHeader<?> extractAllHeaders(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getHeader();
	}

	public static Boolean isTokenExpired(String token) {
		try {
			Date extractExpiration = extractExpiration(token);
			return new Date().after(extractExpiration);
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	public static String getUsername(String authToken) {
		if(authToken==null) {
			return null;
		}
		String token = authToken.startsWith(BEARER) ? authToken.substring(7) : authToken;
		return extractClaim(token, Claims::getSubject);
	}

	public static String getUserRole(String authToken) {
		if(authToken==null) {
			return null;
		}
		String token = authToken.startsWith(BEARER) ? authToken.substring(7) : authToken;
		final JwsHeader<?> jwsHeader = extractAllHeaders(token);
		if (jwsHeader == null) {
			return null;
		}
		return jwsHeader.get(USER_ROLE).toString();
	}
	
	public static String getUserId(String authToken) {
		if(authToken==null) {
			return null;
		}
		String token = authToken.startsWith(BEARER) ? authToken.substring(7) : authToken;
		final JwsHeader<?> jwsHeader = extractAllHeaders(token);
		if (jwsHeader == null) {
			return null;
		}
		return jwsHeader.get(USER_ID).toString();
	}

	public static Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		if (claims == null) {
			return null;
		}
		return claimsResolver.apply(claims);
	}

	public static String getToken(String userName, Long userId, String role) {
		log.debug("TokenServiceImpl :: generateToken() started");
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName,userId, role);
	}

	public static String changeExpiration(String token, Date expiration) {
		log.debug("TokenServiceImpl :: generateToken() started");
		Jws<Claims> parseClaimsJws = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);

		Claims body = parseClaimsJws.getBody();
		JwsHeader<?> header = parseClaimsJws.getHeader();

		return Jwts.builder().setClaims(parseClaimsJws.getBody()).setSubject(body.getSubject())
				.setHeaderParam(USER_ROLE, header.get(USER_ROLE)).setIssuedAt(body.getIssuedAt()).setExpiration(expiration)
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	public static Boolean validateToken(String authToken) {
		String token = authToken.startsWith(BEARER) ? authToken.substring(7) : authToken;
		log.debug("TokenServiceImpl :: validateToken() started");
		return !isTokenExpired(token);
	}

	public static Date buildExprireationDate() {
		return new Date(System.currentTimeMillis() + 1000 * 60 * 30);
	}

}
