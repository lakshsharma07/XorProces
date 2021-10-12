
package com.xoriant.xorpay.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.entity.InvoiceDetailsEntity;
import com.xoriant.xorpay.pojo.InvoiceDetailsPOJO;
import com.xoriant.xorpay.repo.InvoiceDetailsRepo;

@Service
public class InvoiceDetailsService {

  @Autowired
  private InvoiceDetailsRepo invoiceDetailsRepo;
  
  @Autowired
  private ServiceUtil serviceUtil;

  public InvoiceDetailsPOJO getInvoiceDetailsByPaymentId(Long paymentId) {
    List<InvoiceDetailsEntity> invoiceDetailsList = invoiceDetailsRepo.findByPaymentId(paymentId);

    Integer invoiceDetailsCount = invoiceDetailsRepo.countByPaymentId(paymentId);

    InvoiceDetailsPOJO invoiceDetailsPOJO = new InvoiceDetailsPOJO();
    invoiceDetailsPOJO.setInvoiceDetailsList(invoiceDetailsList);
    invoiceDetailsPOJO.setInvoiceCount(invoiceDetailsCount);

    return invoiceDetailsPOJO;
  }

  public InvoiceDetailsPOJO getInvoiceDetailsByPaymentId(Long paymentId, Integer pageNo, Integer pageSize) {
    Pageable paging = serviceUtil.getPagination(pageNo, pageSize);
       
    Page<InvoiceDetailsEntity> invoiceDetailsList = invoiceDetailsRepo.findByPaymentId(paymentId,paging);

    Integer invoiceCount = invoiceDetailsRepo.countByPaymentId(paymentId);
    
    List<InvoiceDetailsEntity> plist = invoiceDetailsList.stream().map(e -> e).collect(Collectors.toList());
   
    return new InvoiceDetailsPOJO(plist,invoiceCount);
    
  }
	
}
