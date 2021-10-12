package com.xoriant.xorpay.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigMasterPOJO {

	private List<String> configEnv;
	private List<String> configName;
	private List<ConfigTypePOJO> configType;
	private List<SrcSysPOJO> sourceSystem;
}