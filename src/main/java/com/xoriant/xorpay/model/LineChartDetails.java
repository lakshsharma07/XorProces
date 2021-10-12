package com.xoriant.xorpay.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LineChartDetails {

	LineChartData lineChartData;
	List<String> labels;
	
}
