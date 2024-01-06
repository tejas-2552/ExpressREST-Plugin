package com.project.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.project.dto.ResponseDto;

public class RestCallUtility {

	public ResponseDto processApiCall(String requestBody, String httpMehtod, String apiUrl, String[][] headersArray) {
		ResponseDto responseModel = new ResponseDto();
		RestCallUtility rest = new RestCallUtility();
		ResponseEntity<String> response;
		try {
			HttpHeaders header = new HttpHeaders();
			if (headersArray != null && headersArray.length > 0) {
				for (int i = 0; i < headersArray.length; i++) {
					header.add(headersArray[i][0], headersArray[i][1]);
				}
			}
			response = rest.callRestAPI(apiUrl, (HttpMethod) getHttpMethod(httpMehtod), requestBody, header);
			System.out.println();
			responseModel.setStatusCode(response.getStatusCode().name());
			responseModel.setResponse(response.getBody());

		} catch (Exception e) {
			responseModel.setResponse(e.getLocalizedMessage());
		}

		return responseModel;
	}

	private static Object getHttpMethod(String httpMehtod) {
		switch (httpMehtod) {
		case "POST":
			return HttpMethod.POST;
		case "GET":
			return HttpMethod.GET;
		case "PUT":
			return HttpMethod.PUT;
		case "DELETE":
			return HttpMethod.DELETE;
		case "PATCH":
			return HttpMethod.PATCH;
		}
		return null;
	}

	public ResponseEntity<String> callRestAPI(String apiUrl, HttpMethod method, String requestBody, HttpHeaders headers)
			throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response;
		try {

			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setConnectTimeout(120000);
			restTemplate.setRequestFactory(requestFactory);
			HttpEntity<?> httpEntity = new HttpEntity<Object>(requestBody, headers);
			response = restTemplate.exchange(apiUrl, method, httpEntity, String.class);

		} catch (HttpStatusCodeException ex) {
			HttpStatus statusCode = ex.getStatusCode();
			String responseBody = ex.getResponseBodyAsString();
			return ResponseEntity.status(statusCode).body(responseBody);
		} catch (ResourceAccessException hostException) {
			throw new Exception("Please confirm URL : " + hostException.getMostSpecificCause().getMessage());
		} catch (IllegalArgumentException e) {
			throw new Exception("Problem occoured while sending request, " + e.getLocalizedMessage());
		} catch (Exception e) {
			throw new Exception("Problem occoured while sending request, Internal server error.");
		}

		return response;
	}

}
