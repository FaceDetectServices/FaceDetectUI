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
package com.yoshio3.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;

/**
 *
 * @author Yoshio Terada
 * @author Toshiaki Maki
 */
@Component
public class StorageService implements Serializable {

	private static final Logger LOGGER = Logger.getLogger(StorageService.class.getName());
	private final static String DEFAULT_ENDPOINT_PROTOCOL = "DefaultEndpointsProtocol=https;";

	public final static String CONTAINER_NAME_FOR_UPLOAD = "uploaded";

	private CloudBlobClient blobClient;

	// Azure Storage サービスに接続するためのキー
	@Value("${vcap.services.storage-service.credentials.accountName:デフォルト値}")
	String accountName;
	@Value("${vcap.services.storage-service.credentials.accountKey:デフォルト値}")
	String accountKey;

	// 初期化
	@PostConstruct
	public void init() {
		try {
			CloudStorageAccount storageAccount = CloudStorageAccount
					.parse(DEFAULT_ENDPOINT_PROTOCOL + accountName + accountKey);
			blobClient = storageAccount.createCloudBlobClient();
			createContainer(CONTAINER_NAME_FOR_UPLOAD);
		}
		catch (URISyntaxException | InvalidKeyException ex) {
			LOGGER.log(Level.SEVERE, "Invalid Account", ex);
		}
	}

	// ディレクトリ(コンテナ)の新規作成
	private void createContainer(String containerName) {
		try {
			String lowercase = containerName.toLowerCase(); // if it include Uppercase 400
															// error
			CloudBlobContainer container = blobClient.getContainerReference(lowercase);
			if (!container.exists()) {
				container.createIfNotExists();

				BlobContainerPermissions permissions = new BlobContainerPermissions();
				permissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
				container.uploadPermissions(permissions);
			}
		}
		catch (URISyntaxException ex) {
			LOGGER.log(Level.SEVERE, "Invalid URISyntax", ex);
		}
		catch (StorageException ste) {
			LOGGER.log(Level.SEVERE, "Invalid Strage type", ste);
		}
	}

	// ファイルのアップロード
	public void uploadFile(UploadedFile file) {
		CloudBlobContainer container;
		try {
			container = blobClient.getContainerReference(CONTAINER_NAME_FOR_UPLOAD);
			CloudBlockBlob blob = container.getBlockBlobReference(file.getFileName());

			blob.upload(file.getInputstream(), file.getSize());
		}
		catch (URISyntaxException | StorageException ex) {
			LOGGER.log(Level.SEVERE, "", ex);
		}
		catch (IOException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		}
	}

	// ファイルのアップロード
	public void uploadFile(byte[] file, String fileName) {
		CloudBlobContainer container;
		try {
			LOGGER.info(() -> "Uploading... " + fileName);
			container = blobClient.getContainerReference(CONTAINER_NAME_FOR_UPLOAD);
			CloudBlockBlob blob = container.getBlockBlobReference(fileName);
			blob.upload(new ByteArrayInputStream(file), file.length);
			LOGGER.info(() -> "Uploaded " + fileName);
		}
		catch (URISyntaxException | StorageException ex) {
			LOGGER.log(Level.SEVERE, "", ex);
		}
		catch (IOException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		}
	}

	// ファイルの一覧取得
	public List<BlobStorageEntity> getAllFiles() {
		List<BlobStorageEntity> entity = new ArrayList<>();
		try {
			CloudBlobContainer container = blobClient
					.getContainerReference(CONTAINER_NAME_FOR_UPLOAD);

			Iterable<ListBlobItem> items = container.listBlobs();
			Spliterator<ListBlobItem> spliterator = items.spliterator();
			Stream<ListBlobItem> stream = StreamSupport.stream(spliterator, false);

			List<CloudBlob> blockBlob = stream.filter(item -> item instanceof CloudBlob)
					.map(item -> (CloudBlob) item).collect(Collectors.toList());

			entity = blockBlob.stream().map(blob -> convertEntity(blob))
					.collect(Collectors.toList());
		}
		catch (URISyntaxException | StorageException ex) {
			LOGGER.log(Level.SEVERE, "", ex);
		}
		return entity;
	}

	// 表示項目出力用のビーンに変換
	private BlobStorageEntity convertEntity(CloudBlob blob) {
		BlobStorageEntity entity = new BlobStorageEntity();
		try {

			BlobProperties properties = blob.getProperties();

			entity.setLastModifyDate(properties.getLastModified());
			entity.setName(blob.getName());
			entity.setSize(properties.getLength());
			entity.setURI(blob.getUri().toString());

		}
		catch (URISyntaxException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		}
		return entity;
	}

	public void deleteAll(String containerName) {
		try {
			CloudBlobContainer container = blobClient
					.getContainerReference(containerName);
			Iterable<ListBlobItem> items = container.listBlobs();
			Spliterator<ListBlobItem> spliterator = items.spliterator();
			Stream<ListBlobItem> stream = StreamSupport.stream(spliterator, false);

			stream.filter(item -> item instanceof CloudBlob).map(item -> (CloudBlob) item)
					.forEach(blob -> {
						try {
							String name = blob.getName();

							CloudBlockBlob delFile;
							delFile = container.getBlockBlobReference(name);
							// Delete the blob.
							delFile.deleteIfExists();
						}
						catch (URISyntaxException | StorageException ex) {
							LOGGER.log(Level.SEVERE, null, ex);
						}
					});
		}
		catch (URISyntaxException | StorageException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		}
	}
}
