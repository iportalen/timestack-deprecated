package com.iportalen.timestack.web.api.firebase.customtoken.phone;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  FirebasePhoneTokenVerificationRequest implements Serializable {
	private static final long serialVersionUID = -1798482912684777449L;
	
	private String phonenumber;
    private String token;
    private String code;

}
