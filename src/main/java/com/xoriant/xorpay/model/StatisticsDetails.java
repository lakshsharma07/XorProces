package com.xoriant.xorpay.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDetails {

	private String statName;
	private String source;
	private long initiatedCount;
	private long inProgressCount;
	private long transmitted;
	private long pendingCount;
	private long acknoledgedCount;
	private long accepetedCount;
	private long rejectedCount;
	private int percentage;

}
