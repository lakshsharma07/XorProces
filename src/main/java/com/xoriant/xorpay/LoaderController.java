package com.xoriant.xorpay;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.data.sync.services.XorProcessService;
import com.xoriant.xorpay.excepions.CustomError;
import com.xoriant.xorpay.parser.service.ConnectorService;

@RestController
@RequestMapping("/load")
public class LoaderController {

	private static final Logger logger = LogManager.getLogger(LoaderController.class);
	private HttpStatus status = null;
	private String type = null;
	private String message = null;
	private String errorMessage = null;
	private String description = null;
	private String res = null;

	@Autowired
	XorProcessService xp;

	@Autowired
	ConnectorService cs;
	
	
	@GetMapping("/data")
	public ResponseEntity<String> loadStgToNrml(HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.info("started");
			String processId = UUID.randomUUID().toString();
			xp.startProcess(processId,request,response);
			logger.info("end");
			status = HttpStatus.OK;
			type = XorConstant.SUCCESS;
			message = "Source added successfully";
		} catch (Exception e) {
			status = HttpStatus.FORBIDDEN;
			type = XorConstant.ERROR;
			message = e.getMessage();
			errorMessage = "Updation addition Failed";
			description = "" + e;
		}
		CustomError c = new CustomError(type, "" + status, message, errorMessage, description);
		res = c.toJsonString(c.toJsonObject());
		return ResponseEntity.ok(res);
	}
	
	@GetMapping("/Callback")
	public void callback(HttpServletRequest request, HttpServletResponse response) {

		try {
			System.out.println("hiiii");
			cs.callBack(request, response);
			System.out.println("byeee");
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			response.sendRedirect("/xorpay/load/data");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}