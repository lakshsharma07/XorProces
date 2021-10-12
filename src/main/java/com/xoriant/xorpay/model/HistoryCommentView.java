package com.xoriant.xorpay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistoryCommentView {

	private Long comment_id;
	private String payment_instruction_id;
	private Long payment_id;
	private String comments;
	private String status;
	private String created_by;
	private String creation_date;
	private String last_update_date;
	private String last_update_by;
	private Long org_id;
	
}

