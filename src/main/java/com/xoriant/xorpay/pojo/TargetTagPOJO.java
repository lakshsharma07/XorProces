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
public class TargetTagPOJO {
	private Integer id;
	private String name;
	private String type;
	//private List<String> columns;
	private List<TagPOJO> columns;
}
