package com.xoriant.xorpay.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PIUIDPojo {

	private Integer id;
	private String country;
	private String profileDes;
	private String profileName;
	private boolean activeInd;
	private String currency;

}
