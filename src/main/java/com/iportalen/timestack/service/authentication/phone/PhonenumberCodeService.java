package com.iportalen.timestack.service.authentication.phone;

import java.util.HashMap;
import java.util.Map;
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
import com.iportalen.timestack.service.sms.SmsMessage;
import com.iportalen.timestack.service.sms.SmsTemplateConstants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PhonenumberCodeService {

	private LoadingCache<String, VerificationCode> phonenumberToCode;

	public PhonenumberCodeService(@Value("${phonenumbercode.maxcache}") Integer maxCache, @Value("${phonenumbercode.expire}") Integer expire) {
		phonenumberToCode = CacheBuilder.newBuilder()
				.maximumSize(maxCache)
				.expireAfterWrite(expire, TimeUnit.HOURS)
				.build(new CacheLoader<String, VerificationCode>() {
					public VerificationCode load(String phonenumber) {
						return new VerificationCode();
					}
				});

	}

	public VerificationCode createCodeForPhonenumber(String phonenumber) throws ExecutionException, NullPointerException {
		Objects.nonNull(phonenumber);
		Strings.isNotBlank(phonenumber);
		if(phonenumberToCode.getIfPresent(phonenumber) != null) {
			log.debug(String.format("Removing existing vericationcode for phonenumber: %s", phonenumber));
			phonenumberToCode.invalidate(phonenumber);
		}
		log.debug(String.format("Creating phonenumber verification for phonenumber: %s", phonenumber));
		return phonenumberToCode.get(phonenumber);
	}

	public void verify(String phonenumber, String token, String code) throws PhonenumberVerificationException, ExecutionException, NullPointerException {
		ObjectUtils.allNotNull(phonenumber, token, code);

		VerificationCode verificationcode = phonenumberToCode.getIfPresent(phonenumber);
		if (Objects.isNull(verificationcode) || !verificationcode.getToken()
				.equals(token)
				|| !verificationcode.getCode()
						.equals(code)) {
			throw new PhonenumberVerificationException();
		} else {
			phonenumberToCode.invalidate(phonenumber);
		}
	}

	public SmsMessage createMessage(String phonenumber, VerificationCode code) {
		Map<String, String> dataModel = new HashMap<String, String>();
		dataModel.put(SmsTemplateConstants.PHONENUMBER_VERIFICATION_SMS_ARG_CODE, code.getCode());
		
		return SmsMessage.builder()
				.recipients(new String[] { phonenumber })
				.template(SmsTemplateConstants.PHONENUMBER_VERIFICATION_SMS_PATH)
				.dataModel(dataModel)
				.build();
	}

}
