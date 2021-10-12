package com.xoriant.xorpay.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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

@Table(name = "XP_PIUID_XML_STRUCTURE")
public class XMLPIUIDStructureEntity {
	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_XML_STR") 
	//@SequenceGenerator(name = "SEQ_XML_STR", sequenceName = "SEQ_XP_PIUID_XML_STRUCTURE_SLNO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "slno")
	private Integer id;
	
	@Column(name = "piuid_seqid")
	private Integer piuidSeqId;
	@Column(name = "struc_master_slno")
	private Integer tagId;
	
	@Column(name = "tag_parent_id")
	private Integer tagParentId;
	@Column(name = "tag_name")
	private String tagName;
	@Column(name = "active_ind")
	private Character activeInd;
	@Column(name = "tag_seq")
	private Integer tagSeq;
	@Column(name = "tag_level")
	private Integer tagLevel;
	

	@Column(name = "creation_date")
	private LocalDateTime creationDate;
	@Column(name = "last_update_date")
	private LocalDateTime lastUpdateDate;
	@Column(name = "effective_start_date")
	private LocalDateTime effectiveStartDate;
	@Column(name = "effective_end_date")
	private LocalDateTime effectiveEndDate;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "last_updated_by")
	private String lastUpdatedBy;

}
