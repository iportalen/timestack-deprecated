package com.iportalen.timestack.web.api.firebase.customtoken.phone;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  FirebasePhoneTokenRequest implements Serializable {
	private static final long serialVersionUID = -6816854803730401339L;
	
	private String phonenumber;
	
}
