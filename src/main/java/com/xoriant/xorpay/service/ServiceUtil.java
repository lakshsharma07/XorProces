package com.xoriant.xorpay.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ServiceUtil {
	
	public Pageable getPagination(Integer pageNo, Integer pageSize) {
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		
		return PageRequest.of(pageNo - 1, pageSize);
	}

}
