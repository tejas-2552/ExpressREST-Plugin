package com.project.dto;

public class ResponseDto {

	public String statusCode;
	public String response;

	public ResponseDto() {
		
	}
	public ResponseDto(String statusCode, String response) {
		super();
		this.statusCode = statusCode;
		this.response = response;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
