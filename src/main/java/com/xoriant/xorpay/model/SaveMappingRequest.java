package com.xoriant.xorpay.model;

import java.util.List;

public class SaveMappingRequest {

	List<SavedMapping> mapping;
	public List<SavedMapping> getMapping() {
		return mapping;
	}

	public void setMapping(List<SavedMapping> mapping) {
		this.mapping = mapping;
	}
}

