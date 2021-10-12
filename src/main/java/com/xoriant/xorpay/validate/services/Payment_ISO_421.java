package com.xoriant.xorpay.validate.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.entity.AggregatedPaymentEntity;
import com.xoriant.xorpay.excepions.CustomError;
import com.xoriant.xorpay.excepions.InvalidDataException;

@Service
public class Payment_ISO_421 {

	CustomError c = null;
	HttpStatus status = null;
	String type = null;
	String message = null;
	String errorMessage = null;
	String description = null;

	public CustomError validateData(List<AggregatedPaymentEntity> tagsList) {
		try {
			tagsList.forEach(tags -> {
				if (null != tags.getCdtrNm() && tags.getCdtrNm().length() > 20) {
					status = HttpStatus.FORBIDDEN;
					type = "Error";
					message = "Failed Payment File Generation";
					errorMessage = "CdtrNm : Creditor name error";
					description = "Creditor name cannot be of more than 20 charector";
					c = new CustomError(type, "" + status, message, errorMessage, description);
					throw new InvalidDataException(c.getErrorMessage());
				}
			});
		} catch (Exception e) {
			return c;
		}
		status = HttpStatus.OK;
		type = "Success";
		message = "Payment File Generated";
		c = new CustomError(type, "" + status, message, errorMessage, description);
		return c;
	}

}
