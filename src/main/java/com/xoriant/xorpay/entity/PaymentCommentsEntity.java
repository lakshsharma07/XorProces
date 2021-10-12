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

@Table(name = "xp_payment_comments")
public class PaymentCommentsEntity {

	@Id
	// @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_G")//,
	// generator="SEQ")
	// @SequenceGenerator(name="SEQ_G", sequenceName="SEQ_XP_PAY_COMM",
	// allocationSize=1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "COMMENT_ID")
	private Integer commentId;

	@Column(name = "PAYMENT_INSTRUCTION_ID")
	private String paymentInstructionId;

	@Column(name = "PAYMENT_ID")
	private String paymentId;
	@Column(name = "COMMENTS")
	private String comments;
	@Column(name = "STATUS")
	private String status;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATION_DATE")
	private LocalDateTime creationDate;
	@Column(name = "LAST_UPDATE_DATE")
	private LocalDateTime lastUpdateDate;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	@Column(name = "ORG_ID")
	private Long orgId;

}
