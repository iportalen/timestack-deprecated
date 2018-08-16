package com.iportalen.timestack.service.firebase;

import org.springframework.stereotype.Service;

import com.iportalen.timestack.conditionals.FirebaseCondition;
import com.iportalen.timestack.config.auth.firebase.FirebaseTokenHolder;
import com.iportalen.timestack.service.shared.FirebaseParser;

@Service
@FirebaseCondition
public class FirebaseServiceImpl implements FirebaseService {
	@Override
	public FirebaseTokenHolder parseToken(String firebaseToken) {
		return new FirebaseParser().parseToken(firebaseToken);
	}
}