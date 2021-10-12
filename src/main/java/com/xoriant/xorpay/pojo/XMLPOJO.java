package com.xoriant.xorpay.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class XMLPOJO {
	private Integer piuid;
	private List<XMLStructurePOJO> xmlStructurePOJO;

}
