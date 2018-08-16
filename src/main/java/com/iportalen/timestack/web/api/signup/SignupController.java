package com.iportalen.timestack.web.api.signup;

import javax.transaction.Transactional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseAuthException;
import com.iportalen.timestack.config.auth.firebase.FirebaseTokenHolder;
import com.iportalen.timestack.service.firebase.FirebaseService;
import com.iportalen.timestack.service.shared.RegisterUserInit;
import com.iportalen.timestack.service.user.UserService;
import com.iportalen.timestack.service.user.UserServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SignupController {
	
	@Autowired(required = false)
	private FirebaseService firebaseService;
	
	@Autowired
	@Qualifier(value = UserServiceImpl.NAME)
	private UserService userService;

	@GetMapping(value = "api/open/firebase/check")
	public void checkToken(@RequestHeader(value ="Authorization") String firebaseToken) throws FirebaseAuthException {
		FirebaseTokenHolder tokenHolder = firebaseService.parseToken(firebaseToken);
		log.info("phonenumber: " + tokenHolder.getPhonenumber());
		
	}
	
	@GetMapping(value = "api/open/firebase/signup")
	public void signUp(@RequestHeader(value ="Authorization") String firebaseToken) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("Token Value: " + firebaseToken);
		log.info("Principal: " + name);
		registerUser(firebaseToken);
	}
	
	@Transactional
	private void registerUser(String firebaseToken) {
		if (Strings.isBlank(firebaseToken)) {
			throw new IllegalArgumentException("FirebaseTokenBlank");
		}
		FirebaseTokenHolder tokenHolder = firebaseService.parseToken(firebaseToken);
		userService.registerUser(new RegisterUserInit(tokenHolder.getUid(), tokenHolder.getPhonenumber()));
	}
}