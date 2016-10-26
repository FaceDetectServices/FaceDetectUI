package com.yoshio3.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Toshiaki Maki
 */
@Component
public class AsyncServiceInvoker {
	private final FaceDetectService faceDetectService;
	private final EmotionService emotionService;

	public AsyncServiceInvoker(FaceDetectService faceDetectService,
			EmotionService emotionService) {
		this.faceDetectService = faceDetectService;
		this.emotionService = emotionService;
	}

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
