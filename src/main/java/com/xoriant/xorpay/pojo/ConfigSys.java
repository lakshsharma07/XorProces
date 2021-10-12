package com.xoriant.xorpay.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigSys {

	private boolean configReload;
	private boolean mockTypeACK;
	private boolean toMock;
	private boolean isCotrolSumCheckReq;

}
