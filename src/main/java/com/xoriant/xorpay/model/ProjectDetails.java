package com.xoriant.xorpay.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDetails {

	private String productName;
	private String license;
	private String agent;
	private String technologyClass;
	private String technology;
	private int tickets;
	private int sales;
	private double  earning;
	
}
