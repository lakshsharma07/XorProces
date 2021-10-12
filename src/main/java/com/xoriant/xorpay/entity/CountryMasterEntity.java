package com.xoriant.xorpay.entity;

import java.math.BigInteger;

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

@Table(name = "XP_COUNTRY_MASTER")
public class CountryMasterEntity {

	@Id
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator
	// ="SEQ_CONFIG_MASTER") // , generator="SEQ")
	// @SequenceGenerator(name = "SEQ_CONFIG_MASTER", sequenceName
	// ="SEQ_XP_CONFIG_MASTER_SLNO", allocationSize = 1)
	@Column(name = "SLNO")
	private Integer id;
	@Column(name = "COUNTRY_NAME")
	private String countryName;
	@Column(name = "COUNTRY_CODE")
	private String countryCode;
	@Column(name = "POPULATION")
	private BigInteger popullation;
	@Column(name = "AREA_KM2")
	private BigInteger areaKm2;
	@Column(name = "ISO2_CODE")
	private String iso2Code;
	@Column(name = "ISO3_CODE")
	private String iso3Code;

}
