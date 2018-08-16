package com.iportalen.timestack.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.iportalen.timestack.domain.model.User;
import com.iportalen.timestack.service.shared.RegisterUserInit;

public interface UserService extends UserDetailsService {

	User registerUser(RegisterUserInit init);
	
	UserDetails loadUserByFirebaseUid(String firebaseUid) throws UsernameNotFoundException;

}