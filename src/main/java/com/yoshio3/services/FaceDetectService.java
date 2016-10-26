package com.yoshio3.services;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Yoshio Terada
 * @author Toshiaki Maki
 */
@Component
public class FaceDetectService {
	private final Logger log = LoggerFactory.getLogger(FaceDetectService.class);
	@Autowired
	RestTemplate restTemplate;

	// @HystrixCommand(fallbackMethod = "getFaceInfoFallback")
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

}
