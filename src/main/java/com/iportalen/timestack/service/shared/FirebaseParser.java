package com.iportalen.timestack.service.shared;

import java.util.ArrayList;

import org.apache.logging.log4j.util.Strings;

import com.google.api.client.util.ArrayMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.iportalen.timestack.config.auth.firebase.FirebaseTokenHolder;
import com.iportalen.timestack.service.exception.FirebaseTokenInvalidException;

public class FirebaseParser {

	public FirebaseTokenHolder parseToken(String idToken) {
		if (Strings.isBlank(idToken)) {
			throw new IllegalArgumentException("FirebaseTokenBlank");
		}
		try {
			FirebaseToken firebaseToken = FirebaseAuth.getInstance()
					.verifyIdToken(idToken);

			return FirebaseTokenHolder.builder()
					.token(firebaseToken)
					.email(firebaseToken.getEmail())
					.name(firebaseToken.getName())
					.issuer(firebaseToken.getIssuer())
					.uid(firebaseToken.getUid())
					.phonenumber(extractPhonenumber(firebaseToken))
					.build();
		} catch (Exception e) {
			throw new FirebaseTokenInvalidException(e.getMessage());
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String extractPhonenumber(FirebaseToken firebaseToken) {
		try {
			return ((ArrayList<String>) ((ArrayMap) ((ArrayMap) firebaseToken.getClaims().get("firebase")).get("identities")).get("phone")).get(0);
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	private String extractGoogleId(FirebaseToken firebaseToken) {
		try {
			return ((ArrayList<String>) ((ArrayMap) ((ArrayMap) firebaseToken.getClaims().get("firebase")).get("identities")).get("google.com")).get(0);
		} catch (Exception e) {
			return null;
		}
	}

}
