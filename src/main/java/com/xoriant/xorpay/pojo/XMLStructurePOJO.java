package com.xoriant.xorpay.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class XMLStructurePOJO {

	private Integer id;
	private Integer pid;
	private String name;
	private Integer tagSeq;
	private boolean isActive;
	private boolean hasChild;
	private boolean expanded;

}
