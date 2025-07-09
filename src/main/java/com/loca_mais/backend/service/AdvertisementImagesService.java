package com.loca_mais.backend.service;

import com.loca_mais.backend.dao.AdvertisementImagesDAO;
import com.loca_mais.backend.model.AdvertisementImagesEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdvertisementImagesService {

    private final AdvertisementImagesDAO advertisementImagesDAO;

    private final String UPLOAD_DIR = "uploads/advertisements/";

    public List<AdvertisementImagesEntity> save(MultipartFile[] files, Integer advertisementId) throws IOException {
        List<AdvertisementImagesEntity> savedImages = new ArrayList<>();

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);

                Files.copy(file.getInputStream(), filePath);

                AdvertisementImagesEntity imageEntity = new AdvertisementImagesEntity();
                imageEntity.setUrl("/" + UPLOAD_DIR + fileName);
                imageEntity.setAdvertisementId(advertisementId);

                advertisementImagesDAO.save(imageEntity);
                savedImages.add(imageEntity);
            }
        }
        return savedImages;
    }

    public List<String> getImagesByAdvertisementId(Integer advertisementId) {
        return advertisementImagesDAO.findByAdvertisementId(advertisementId).stream().map(AdvertisementImagesEntity::getUrl).toList();
    }

    public void delete(Integer advertisementId) {
        advertisementImagesDAO.delete(advertisementId);
    }
}