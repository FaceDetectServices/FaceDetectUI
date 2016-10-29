package com.yoshio3.backingbean;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.yoshio3.services.EmotionService;
import com.yoshio3.services.FaceDetectService;

/**
 *
 * @author Toshiaki Maki
 */
@SessionScope
@Component("loadGenerator")
public class LoadGenerator {
	private final FaceDetectService faceDetectService;
	private final EmotionService emotionService;

	public LoadGenerator(FaceDetectService faceDetectService,
			EmotionService emotionService) {
		this.faceDetectService = faceDetectService;
		this.emotionService = emotionService;
	}

	public void sentRequestToFaceDetectService() {
		for (int i = 0; i < 30; i++) {
			this.faceDetectService
					.getFaceInfo("http://optipng.sourceforge.net/pngtech/img/lena.png");
		}
	}

	public void sentRequestToEmotionService() {
		for (int i = 0; i < 30; i++) {
			this.emotionService.getEmotionalInfo(
					"http://optipng.sourceforge.net/pngtech/img/lena.png");
		}
	}

	public void killFaceDetectService() {
		this.faceDetectService.kill();
	}

	public void killEmotionService() {
		this.emotionService.kill();
	}
}
