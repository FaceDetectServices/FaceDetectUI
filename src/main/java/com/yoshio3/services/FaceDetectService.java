package com.yoshio3.services;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 *
 * @author Yoshio Terada
 */
@Component
public class FaceDetectService {
	@Autowired
	RestTemplate restTemplate;

	//@HystrixCommand(fallbackMethod = "getFaceInfoFallback")
	public FaceAttributes getFaceInfo(String pictURI) {
		URI uri = UriComponentsBuilder
				.fromHttpUrl("http://FaceDetectService/api/facedetect")
				.queryParam("url", pictURI).build().toUri();
		return restTemplate.getForObject(uri, FaceAttributes.class);
	}

	public FaceAttributes getFaceInfoFallback(String pictURI) {
		return new FaceAttributes("??", -100.0);
	}

}
