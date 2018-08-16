package com.iportalen.timestack.web.api.firebase.customtoken.phone;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  FirebasePhoneTokenResponse implements Serializable {
	private static final long serialVersionUID = 4861841561412717269L;
	
	private String token;
}
