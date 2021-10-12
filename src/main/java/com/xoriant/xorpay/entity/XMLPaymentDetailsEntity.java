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

@Table(name = "xp_org_name_view")
public class XMLPaymentDetailsEntity {

	@Id
	@Column(name = "SLNO")
	private Integer id;

	@Column(name = "ORG_NAME")
	private String orgName;
	
}