package com.iportalen.timestack.web.api.firebase.customtoken.phone;

import java.util.concurrent.ExecutionException;

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
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.iportalen.timestack.service.authentication.phone.InvalidPhonenumberException;
import com.iportalen.timestack.service.authentication.phone.PhonenumberCodeService;
import com.iportalen.timestack.service.authentication.phone.PhonenumberVerificationException;
import com.iportalen.timestack.service.authentication.phone.VerificationCode;
import com.iportalen.timestack.service.sms.MalformedSmsMessageException;
import com.iportalen.timestack.service.sms.SmsMessage;
import com.iportalen.timestack.service.sms.SmsService;
import com.iportalen.timestack.web.api.firebase.customtoken.AuthenticationException;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/firebase/phonenumber")
@Slf4j
public class FirebasePhoneTokenController {

    @Autowired
    private PhonenumberCodeService phonenumberCodeService;
    
    @Autowired
    private SmsService smsService;
    
    @PostMapping("/token")
    public ResponseEntity<?> requestToken(@RequestBody FirebasePhoneTokenRequest phonenumberCodeRequest, HttpServletResponse response) throws AuthenticationException {
    	try {
    		PhoneNumber number = PhoneNumberUtil.getInstance().parse(phonenumberCodeRequest.getPhonenumber(), "");
    		if(!PhoneNumberUtil.getInstance().isValidNumber(number)) throw new InvalidPhonenumberException();
    		
    		VerificationCode code = phonenumberCodeService.createCodeForPhonenumber(phonenumberCodeRequest.getPhonenumber());
    		SmsMessage message = phonenumberCodeService.createMessage(phonenumberCodeRequest.getPhonenumber(), code);
    		smsService.sendMessage(message);
    		return ResponseEntity.ok().body(new FirebasePhoneTokenResponse(code.getToken()));
		} catch (NullPointerException | ExecutionException e) {
			return ResponseEntity.badRequest().body(null);
		} catch (MalformedSmsMessageException e) {
			log.error("Message was malformed!", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		} catch (NumberParseException | InvalidPhonenumberException e) {
			return ResponseEntity.badRequest().body(null);
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
		} catch (NullPointerException | PhonenumberVerificationException | ExecutionException | FirebaseAuthException e) {
			return ResponseEntity.badRequest().body(null);
		}
    }
	
    @ExceptionHandler({PhonenumberVerificationException.class})
    public ResponseEntity<String> handleAuthenticationException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

}
