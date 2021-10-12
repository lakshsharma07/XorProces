package com.xoriant.xorpay.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Statistics{

	String statName;
	
	//total count
	long count;
	int percentage;
	
}
