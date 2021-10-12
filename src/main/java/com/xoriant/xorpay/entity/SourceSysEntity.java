package com.xoriant.xorpay.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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

@Table(name = "XP_SRC_SYSTEM")
public class SourceSysEntity {

	@Id
	// @GeneratedValue(strategy=GenerationType.AUTO)//, generator="SEQ")
	// @SequenceGenerator(name="SEQ", sequenceName="SEQ_XP_SCHEDULED_STATUS",
	// allocationSize=1)
	@Column(name = "SRC_SYS_ID")
	private Integer id;
	@Column(name = "SRC_SYSTEM")
	private String srcSystemSort;
	@Column(name = "USER_SRC_SYS_DEF")
	private String srcSystemDesc;
	@Column(name = "USER_INTF")
	private String srcSystemLong;
	@Column(name = "WATERMARK")
	private String watermark;

}
