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

import com.yoshio3.services.BlobStorageEntity;
import com.yoshio3.services.StorageService;
import java.io.Serializable;
import java.util.List;
import org.primefaces.model.UploadedFile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 *
 * @author Yoshio Terada
 * @author Toshiaki Maki
 */
@SessionScope
@Component("filelist")
public class FileListView implements Serializable {
    private List<UploadedFile> files;
    private List<BlobStorageEntity> entities;

    private final StorageService service;

    public FileListView(StorageService service) {
        this.service = service;
    }

    public List<UploadedFile> getFile() {
        return files;
    }

    public void setFile(List<UploadedFile> files) {
        this.files = files;
    }

    /**
     * @return the entities
     */
    public List<BlobStorageEntity> getEntities() {
        return service.getAllFiles();
    }

    /**
     * @param entities the entities to set
     */
    public void setEntities(List<BlobStorageEntity> entities) {
        this.entities = entities;
    }


    public void deleteAll() {
        service.deleteAll(PhotoUploader.UPLOAD_DIRECTORY_NAME_OF_BLOB);
    }

}
