package com.xoriant.xorpay.pojo;

import java.util.List;

import com.xoriant.xorpay.entity.SourceSysEntity;
import com.xoriant.xorpay.entity.SystemDataTypeEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddSourcePOJO {

	private List<XMLTagPOJO> xmlTagList;
	private List<SystemDataTypeEntity> dataTypeList;
	private SourceSysEntity sourceSystem;
}