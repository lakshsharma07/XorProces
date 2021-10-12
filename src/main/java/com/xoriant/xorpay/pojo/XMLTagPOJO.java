package com.xoriant.xorpay.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class XMLTagPOJO {

	private Integer id;
	private String tagName;
	private String xpath;
	private String tagInfoType;
	private String srcColInfoType;
	
	public XMLTagPOJO(Integer id,String tagName,String xpath) {
		this.id = id;
		this.tagName = tagName;
		this.xpath = xpath;
	}

}
