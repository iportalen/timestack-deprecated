package com.iportalen.timestack.config.auth.firebase;

import com.google.firebase.auth.FirebaseToken;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FirebaseTokenHolder {
	
	private FirebaseToken token;
	private String email;
	private String issuer;
	private String name;
	private String uid;
	private String phonenumber;
	private String googleId;

}