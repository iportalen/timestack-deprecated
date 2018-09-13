package com.iportalen.timestack.web.api.firebase.customtoken.phone;

import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.iportalen.timestack.service.verification.sms.SMSVerification;
import com.iportalen.timestack.service.verification.sms.SMSVerificationCreationException;
import com.iportalen.timestack.service.verification.sms.SMSVerificationSender;
import com.iportalen.timestack.service.verification.sms.SMSVerificationService;
import com.iportalen.timestack.service.verification.sms.VerificationCodeVerificationException;
import com.iportalen.timestack.web.api.firebase.customtoken.AuthenticationException;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/firebase/phonenumber")
@Slf4j
public class FirebasePhoneTokenController {

    @Autowired
    private SMSVerificationService phonenumberCodeService;
    
    @Autowired
    SMSVerificationSender smsVerificationSender;
    
    @PostMapping("/token")
    public ResponseEntity<?> requestToken(@RequestBody FirebasePhoneTokenRequest phonenumberCodeRequest, HttpServletRequest request) throws AuthenticationException {
    	String phonenumber = phonenumberCodeRequest.getPhonenumber();
    	log.info("Phonenumber " + phonenumber + " requests verification code");
		try {
			validatePhonenumber(phonenumber);
			SMSVerification smsVerification = createVerificationCode(phonenumber);
			smsVerificationSender.send(phonenumber, smsVerification.getCode(), request.getLocale());
    		return ResponseEntity.ok().body(new FirebasePhoneTokenResponse(smsVerification.getToken()));
		} catch (TokenRequestException e) {
			return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
		}
    }

	private void validatePhonenumber(String phonenumber) {
		try {
			PhoneNumber number = PhoneNumberUtil.getInstance().parse(phonenumber, "");
			if (!PhoneNumberUtil.getInstance().isValidNumber(number))
				throw new InvalidPhonenumberException();
		} catch (Exception e) {
			throw new TokenRequestException(HttpStatus.BAD_REQUEST, "Invalid phonenumber!");
		}
	}
	
	private SMSVerification createVerificationCode(String phonenumber) {
		try {
			return phonenumberCodeService.create(phonenumber);
		} catch (SMSVerificationCreationException e) {
			throw new TokenRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
		}
	}

	@PostMapping("/verify")
    public ResponseEntity<?> verifyPhonenumber(@RequestBody FirebasePhoneTokenVerificationRequest authRequest, HttpServletResponse response) throws AuthenticationException {
		try {
			phonenumberCodeService.verify(authRequest.getPhonenumber(), authRequest.getToken(), authRequest.getCode());
			CreateRequest createReq = new CreateRequest();
			createReq.setPhoneNumber(authRequest.getPhonenumber());
			createReq.setDisabled(false);
			UserRecord userRecord = null;
			try {
				userRecord = FirebaseAuth.getInstance().createUser(createReq);
			} catch (FirebaseAuthException e) {
				try {
					userRecord = FirebaseAuth.getInstance().getUserByPhoneNumber(authRequest.getPhonenumber());
				} catch (FirebaseAuthException e1) {
					return ResponseEntity.badRequest().body(null);
				}
			}
			response.setHeader("X-Firebase-CustomToken", FirebaseAuth.getInstance().createCustomToken(userRecord.getUid()));
			return ResponseEntity.ok().body(null);
		} catch (NullPointerException | VerificationCodeVerificationException | ExecutionException | FirebaseAuthException e) {
			return ResponseEntity.badRequest().body(null);
		}
    }
	
    @ExceptionHandler({VerificationCodeVerificationException.class})
    public ResponseEntity<String> handleAuthenticationException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

}
