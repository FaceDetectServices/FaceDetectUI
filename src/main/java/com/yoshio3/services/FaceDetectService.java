package com.yoshio3.services;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 *
 * @author Yoshio Terada
 * @author Toshiaki Maki
 */
@Component
public class FaceDetectService {
	private final Logger log = LoggerFactory.getLogger(FaceDetectService.class);
	private final RestTemplate restTemplate;

	public FaceDetectService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@HystrixCommand(fallbackMethod = "getFaceInfoFallback")
	public FaceAttributes getFaceInfo(String pictURI) {
		log.info("start get face => {}", pictURI);
		URI uri = UriComponentsBuilder
				.fromHttpUrl("http://FaceDetectService/api/facedetect")
				.queryParam("url", pictURI).build().toUri();
		return restTemplate.getForObject(uri, FaceAttributes.class);
	}

	public FaceAttributes getFaceInfoFallback(String pictURI) {
		return new FaceAttributes("??", -100.0);
	}

	public void kill() {
		log.info("Kill Face Detect API");
		URI uri = UriComponentsBuilder.fromHttpUrl("http://FaceDetectService/api/kill")
				.build().toUri();
		try {
			restTemplate.getForObject(uri, Void.class);
		}
		catch (RestClientException e) {
			log.info("Ignore exception => message = {}", e.getMessage());
		}
	}
}
