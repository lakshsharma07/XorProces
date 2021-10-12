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

@Table(name = "XP_SYSTEM_DB_DATATYPE")
public class SystemDataTypeEntity {

	@Id
	@Column(name = "SLNO")
	private Integer id;
	@Column(name = "SYSTEM_DB_DATATYPE")
	private String systemDataType;
	
}
