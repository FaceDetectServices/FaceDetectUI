package com.yoshio3.services;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class EmotionService {
	@Autowired
	RestTemplate restTemplate;

	//@HystrixCommand(fallbackMethod = "getEmotionalInfoFallback")
	public EmotionAttributes getEmotionalInfo(String pictURI) {
		URI uri = UriComponentsBuilder
				.fromHttpUrl("http://EmotionService/api/emotionservice")
				.queryParam("url", pictURI).build().toUri();
		return restTemplate.getForObject(uri, EmotionAttributes.class);
	}

	public EmotionAttributes getEmotionalInfoFallback(String pictURI) {
		return new EmotionAttributes(-100.0, -100.0, -100.0, -100.0, -100.0, -100.0,
				-100.0, -100.0);
	}
}
