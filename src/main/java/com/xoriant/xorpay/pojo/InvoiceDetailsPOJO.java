/**
 * 
 */
package com.xoriant.xorpay.pojo;

import java.util.List;

import com.xoriant.xorpay.entity.InvoiceDetailsEntity;

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
public class InvoiceDetailsPOJO {
	List<InvoiceDetailsEntity> invoiceDetailsList;
	Integer invoiceCount;
}
