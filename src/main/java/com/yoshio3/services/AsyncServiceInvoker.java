package com.yoshio3.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Toshiaki Maki
 */
@Component
public class AsyncServiceInvoker {
	@Autowired
	FaceDetectService faceDetectService;
	@Autowired
	EmotionService emotionService;

	@Async
	public CompletableFuture<FaceAttributes> getFaceInfo(String pictURI) {
		return CompletableFuture.completedFuture(faceDetectService.getFaceInfo(pictURI));
	}

	@Async
	public CompletableFuture<EmotionAttributes> getEmotionInfo(String pictURI) {
		return CompletableFuture
				.completedFuture(emotionService.getEmotionalInfo(pictURI));
	}
}
