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
@Table(name = "XP_PIUID_INFORMATION")
public class PIUIDEntity {

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PU")
	//@SequenceGenerator(name = "SEQ_PU", sequenceName = "SEQ_XP_PUID_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "piuid_seqid")
	private Integer id;

	@Column(name = "piuid_number")
	private Integer piuidNumber;

	@Column(name = "is_xmlbuilder_d")
	private Character isXmlbuilderD;
	
	@Column(name = "profilename")
	private String profileName;
	
	@Column(name = "profile_desc")
	private String profileDes;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "creation_date")
	private LocalDateTime createdDate;
	@Column(name = "last_updated_by")
	private String updatedBy;
	@Column(name = "last_update_date")
	private LocalDateTime updatedDate;
	@Column(name = "active_ind")
	private Character activeInd;
	@Column(name = "effective_start_date")
	private LocalDateTime effecStartDate;
	@Column(name = "effective_end_date")
	private LocalDateTime EffecEndDate;
	@Column(name = "country_slno")
	private Integer countryId;
	@Column(name = "currency_slno")
	private Integer currencyId;

}
