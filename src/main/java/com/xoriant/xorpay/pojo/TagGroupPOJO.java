/**
 * 
 */
package com.xoriant.xorpay.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author XOR FRAMEWORK team
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagGroupPOJO {
	private Integer id;
	private String tagHeading;
	private List<TagPOJO> tagPojoList;
}
