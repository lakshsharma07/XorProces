package com.xoriant.xorpay.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = "XP_SCHEDULE_STATUS")
public class XpScheduleStatusEntity {

	@Id
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_SC")
	//@SequenceGenerator(name="SEQ_SC", sequenceName="SEQ_XP_SCHEDULED_STATUS", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SLNO")
	private Integer id;
	
	@Column(name = "REQUEST_VIEW_NAME")
	private String requestViewName;
	
	@Column(name = "REQUESTNAME")
	private String requestName;
	@Column(name = "STATUS")
	private String status;
	@Column(name = "FREQUENCY")
	private String frequency;
	@Column(name = "STARTDATE")
	private LocalDateTime startDate;
	@Column(name = "ENDDATE")
	private LocalDateTime endDate;
	@Column(name = "SCHEDULE")
	private Character schedule;
	@Column(name = "LASTRUNID")
	private Integer lastRunId;
	@Column(name = "LASTRUNTIME")
	private LocalDateTime lastRunTime;
	@Column(name = "ENV")
	private String env;
	
}
