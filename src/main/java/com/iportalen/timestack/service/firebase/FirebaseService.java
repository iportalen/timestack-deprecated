package com.iportalen.timestack.service.firebase;

import com.iportalen.timestack.config.auth.firebase.FirebaseTokenHolder;

public interface FirebaseService {

	FirebaseTokenHolder parseToken(String idToken);

}
