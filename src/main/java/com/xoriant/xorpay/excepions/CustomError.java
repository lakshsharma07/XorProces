package com.xoriant.xorpay.excepions;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomError {

	private String error;
	private HttpStatus statustype;
	private String status;
	private String message;
	private String errorMessage;
	private String description;
	private String type;
	
	public CustomError(String type, String scode,String message,String errorMessage,  String description) {
		this.type = type;
		this.status = scode;
		this.message = message;
		this.errorMessage = errorMessage;
		this.description = description;
	}
	
	public JSONObject toJsonObject() {
		JSONObject jo = new JSONObject();
		jo.put("type", type);
		jo.put("status", status);
		jo.put("Message", message);
		jo.put("Error", errorMessage);
		jo.put("Description", description);
		return jo;
	}
	
	public String toJsonString(Object c) {
		ObjectMapper obj = new ObjectMapper();
		String json = null;
		try {
			 json = obj.writeValueAsString(c);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	


	
}
