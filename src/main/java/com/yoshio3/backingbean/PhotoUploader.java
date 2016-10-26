/*
* Copyright 2016 Yoshio Terada
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
 */
package com.yoshio3.backingbean;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.primefaces.event.CaptureEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yoshio3.services.AsyncServiceInvoker;
import com.yoshio3.services.StorageService;

//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//import java.util.logging.Level;
//import javax.inject.Inject;
//import javax.ws.rs.core.Response;

//import com.yoshio3.services.EmotionService;
//import com.yoshio3.services.FaceDetectService;
//import com.yoshio3.services.StorageService;
//import com.yoshio3.services.entities.EmotionResponseJSONBody;
//import com.yoshio3.services.entities.FaceDetectResponseJSONBody;

/**
 *
 * @author Yoshio Terada
 */
@Component("photoup")
@Scope("request")
public class PhotoUploader implements Serializable {

	private static final Logger LOGGER = Logger.getLogger(PhotoUploader.class.getName());

	// Please change following 4 lines (Blob URL)
	protected final static String UPLOAD_DIRECTORY_NAME_OF_BLOB = "uploaded";
	private final static String AZURE_BLOG_UPLOAD_URL = "https://yoshiofileup.blob.core.windows.net/"
			+ UPLOAD_DIRECTORY_NAME_OF_BLOB + "/";
	private final static String EMOTIONAL_API_SUBSCRIPTION = "********************************";
	private final static String FACE_API_SUBSCRIPTION = "********************************";

	private final static String IMAGE_FORMAT_EXTENSION = ".jpg";

	private Double anger;
	private Double contempt;
	private Double disgust;
	private Double fear;
	private Double happiness;
	private Double neutral;
	private Double sadness;
	private Double surprise;
	private Double age;
	private String gender;

	@Autowired
	AsyncServiceInvoker serviceInvoker;

	@Autowired
	StorageService storageService;

	private String fileURL;

	public void sendPhoto(CaptureEvent captureEvent) {
		// try {
		// ファイル名の作成
		UUID uuid = UUID.randomUUID();
		String fileName = uuid.toString() + IMAGE_FORMAT_EXTENSION;
		byte[] data = captureEvent.getData();
		// // Azure Storage にファイルのアップロード
		storageService.uploadFile(data, fileName);
		// // アップロードされたファイルの URL
		fileURL = AZURE_BLOG_UPLOAD_URL + fileName;
		CompletableFuture<Void> f = serviceInvoker.getFaceInfo(fileURL)
				.thenAccept(face -> {
					this.age = face.getAge();
					this.gender = face.getGender();
				});
		CompletableFuture<Void> e = serviceInvoker.getEmotionInfo(fileURL)
				.thenAccept(emotion -> {
					this.anger = emotion.getAnger();
					this.contempt = emotion.getContempt();
					this.disgust = emotion.getDisgust();
					this.fear = emotion.getFear();
					this.happiness = emotion.getHappiness();
					this.neutral = emotion.getNeutral();
					this.sadness = emotion.getSadness();
					this.surprise = emotion.getSurprise();
				});
		try {
			CompletableFuture.allOf(f, e).get();
		}
		catch (InterruptedException | ExecutionException ex) {
			ex.printStackTrace();
		}
		// System.out.println(faceDetectService.getFaceInfo("https://yoshiofileup.blob.core.windows.net/uploaded/5a0cbbcf-5645-4945-bf76-488cae3ecdfc.jpg"));
	}

	public String getGender() {
		if ("male".equals(gender)) {
			return "男性";
		}
		else if ("female".equals(gender)) {
			return "女性";
		}
		return gender;
	}

	public String getFileURL() {
		return fileURL;
	}

	/**
	 * @return the anger
	 */
	public Double getAnger() {
		return anger;
	}

	/**
	 * @return the contempt
	 */
	public Double getContempt() {
		return contempt;
	}

	/**
	 * @return the disgust
	 */
	public Double getDisgust() {
		return disgust;
	}

	/**
	 * @return the fear
	 */
	public Double getFear() {
		return fear;
	}

	/**
	 * @return the happiness
	 */
	public Double getHappiness() {
		return happiness;
	}

	/**
	 * @return the neutral
	 */
	public Double getNeutral() {
		return neutral;
	}

	/**
	 * @return the sadness
	 */
	public Double getSadness() {
		return sadness;
	}

	/**
	 * @return the surprise
	 */
	public Double getSurprise() {
		return surprise;
	}

	public Double getAge() {
		return age;
	}
}
