package com.xoriant.xorpay.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageAccessPOJO {
	private Integer id;
	private Integer pageId;
	private String pageName;
	private String pageDesc;
	private Integer roleId;
	private String roleName;
	private Boolean access;
}
