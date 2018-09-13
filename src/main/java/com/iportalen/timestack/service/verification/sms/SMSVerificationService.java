package com.iportalen.timestack.service.verification.sms;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SMSVerificationService {
	
	private LoadingCache<String, SMSVerification> phonenumberToCode;

	public SMSVerificationService(@Value("${phonenumbercode.maxcache}") Integer maxCache, @Value("${phonenumbercode.expire}") Integer expire) {
		phonenumberToCode = CacheBuilder.newBuilder()
				.maximumSize(maxCache)
				.expireAfterWrite(expire, TimeUnit.HOURS)
				.build(new CacheLoader<String, SMSVerification>() {
					public SMSVerification load(String phonenumber) {
						return new SMSVerification();
					}
				});

	}

	public SMSVerification create(String phonenumber) throws SMSVerificationCreationException {
		try {
			Objects.nonNull(phonenumber);
			Strings.isNotBlank(phonenumber);
			if(phonenumberToCode.getIfPresent(phonenumber) != null) {
				log.debug(String.format("Removing existing vericationcode for phonenumber: %s", phonenumber));
				phonenumberToCode.invalidate(phonenumber);
			}
			log.debug(String.format("Creating phonenumber verification for phonenumber: %s", phonenumber));
			return phonenumberToCode.get(phonenumber);
		} catch (Exception e) {
			log.error("Could not create verification code for phonenumber: " + phonenumber);
			throw new SMSVerificationCreationException(e);
		}
	}

	public void verify(String phonenumber, String token, String code) throws VerificationCodeVerificationException, ExecutionException, NullPointerException {
		ObjectUtils.allNotNull(phonenumber, token, code);

		SMSVerification verificationcode = phonenumberToCode.getIfPresent(phonenumber);
		if (Objects.isNull(verificationcode) || !verificationcode.getToken()
				.equals(token)
				|| !verificationcode.getCode()
						.equals(code)) {
			throw new VerificationCodeVerificationException();
		} else {
			phonenumberToCode.invalidate(phonenumber);
		}
	}

}
