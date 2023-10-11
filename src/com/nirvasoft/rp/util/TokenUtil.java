package com.nirvasoft.rp.util;

import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.json.JSONException;

import com.google.gson.Gson;
import com.nirvasoft.rp.dao.DAOManager;
import com.nirvasoft.rp.framework.ServerSession;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import password.DESedeEncryption;

public class TokenUtil {
	public static final long JWT_TOKEN_VALIDITY = (long) (24 * 60 * 60 * 1000 );// 24 hour
	public static final long JWT_TOKEN_ONE_MIN_VALIDITY = (long) (1 * 60 * 60 * 1000);// 1 hour
	private static final String SECRET_KEY = "3ay3ay1032smepapz18acp$0wf";// f815f59b9fde0863ced32a0d313d45370f6cdfc2def08b12
	
	/**
	 * This method is used to get username from token
	 * 
	 * @param token
	 *            token
	 * @return username
	 */
	public static String getUsernameFromToken(String token) {
		Claims claim = getAllClaimsFromToken(token);
		if (claim != null) {
			return claim.getSubject();
		} else {
			return null;
		}
	}

	/**
	 * This method is used to get the expired date from token
	 * 
	 * @param token
	 *            token
	 * @return expired date
	 */
	public static Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}

	/**
	 * This method is used to get claims from token
	 * 
	 * @param token
	 *            token
	 * @return claims
	 */
	private static Claims getAllClaimsFromToken(String token) {
		try {
			return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)).parseClaimsJws(token)
					.getBody();
		} catch (ExpiredJwtException e) {
			// e.printStackTrace();
			return null;
		} catch (UnsupportedJwtException e) {
			// e.printStackTrace();
			return null;
		} catch (MalformedJwtException e) {
			// e.printStackTrace();
			return null;
		} catch (SignatureException e) {
			// e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			// e.printStackTrace();
			return null;
		} catch (ArrayIndexOutOfBoundsException e) {
			// e.printStackTrace();
			return null;
		}
	}

	/**
	 * This method is used to check the token is expired or not
	 * 
	 * @param token
	 *            token
	 * @return true or false
	 */
	public static Boolean isTokenExpired(String token) {
		try {
			Date expiration = getAllClaimsFromToken(token).getExpiration();
			return expiration.before(new Date());
		} catch (Exception e) {
			// e.printStackTrace();
			return true;
		}
	}

	/**
	 * This method is used to generate the token
	 * 
	 * @param subject
	 *            subject
	 * @return token
	 */
	public static String generateToken(String subject, boolean isExpiredInclude) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, subject, isExpiredInclude);
	}

	public static String generateTokenForOTP(String subject) {
		Map<String, Object> claims = new HashMap<>();
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_ONE_MIN_VALIDITY))
				.signWith(signatureAlgorithm, signingKey).compact();
	}

	/**
	 * This method is used to generate the token
	 * 
	 * @param claims
	 *            claim object
	 * @param username
	 *            username
	 * @return generated token
	 */
	private static String doGenerateToken(Map<String, Object> claims, String subject, boolean isExpiredInclude) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		if (isExpiredInclude) {
			System.out.println("currentTime"+new Date(System.currentTimeMillis()));
			System.out.println("addTime"+new Date(JWT_TOKEN_VALIDITY));
			System.out.println("Exp:"+new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY));
			return Jwts.builder().setClaims(claims).setSubject(subject)
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
					.signWith(signatureAlgorithm, signingKey).compact();
			
			
		} else {
			return Jwts.builder().setClaims(claims).setSubject(subject)
					.setIssuedAt(new Date(System.currentTimeMillis())).signWith(signatureAlgorithm, signingKey)
					.compact();
		}
		
	}

	/**
	 * This method is used to check the token is valid or not
	 * 
	 * @param token
	 *            token
	 * @param uuid
	 * @return true or false
	 */
	public static Boolean validToken(String uuid, String token) {
		if (!isTokenExpired(token)) {
			Map<String, Object> resultMap = new Gson().fromJson(getUsernameFromToken(token), Map.class);
			if (resultMap != null) {
				return resultMap.get("uuid").equals(uuid);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public static Boolean validSkey(String regKey,String mobile,String accno, String skey) {
		if (!isTokenExpired(skey)) {
			Map<String, Object> resultMap = new Gson().fromJson(getUsernameFromToken(skey), Map.class);
			if (resultMap != null) {
				return resultMap.get("regKey").equals(regKey) && resultMap.get("mobile").equals(mobile) && resultMap.get("accno").equals(accno);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public static Boolean validSpkey(String toWLID,String mobile,String ckey, String spkey) {
		if (!isTokenExpired(spkey)) {
			Map<String, Object> resultMap = new Gson().fromJson(getUsernameFromToken(spkey), Map.class);
			if (resultMap != null) {
				return resultMap.get("toWLID").equals(toWLID) && resultMap.get("mobile").equals(mobile) && resultMap.get("ckey").equals(ckey);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static long getLong(Object o) {
		try {
			return o instanceof String ? Long.parseLong((String) o)
					: o instanceof Integer ? (int) o
							: o instanceof Long ? (long) o
									: o instanceof Double ? (new Double((double) o)).longValue() : 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static String encryptPIN(String p) {
		String ret = "";
		try {
			DESedeEncryption myEncryptor = new DESedeEncryption();
			ret = myEncryptor.encrypt(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String getMsgbyCode(String code) {
		Connection ret = null;
		String desc = "";
		try {
			ArrayList<String> oracleConnList;
			oracleConnList = ReadFile.readConnection(DAOManager.AbsolutePath + "WEB-INF//reference//responseMsg.txt");
			for(int i=0;i<oracleConnList.size(); i++) {
				if(oracleConnList.get(i).contains(code))
					desc = oracleConnList.get(i).split(code+":")[1].trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
			desc="";
		}
		return desc;
	}
	
	public static String getValueOfToken(String code, String token) {
		String value="";
			Map<String, Object> resultMap = new Gson().fromJson(getUsernameFromToken(token), Map.class);
			if (resultMap != null) {
				if(resultMap.containsKey(code)){
					value = resultMap.get(code).toString();
				}
			}
		return value;
	}
}
