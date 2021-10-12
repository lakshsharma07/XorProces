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
public class MappedSourceTargetPOJO {
	//private Map<String, List<TagPOJO>> mappedSourceTaget;
	private List<TargetTagPOJO> sourcetargeList;
	private Integer sourceSystem;
	private Integer piuidSeqId;
}

