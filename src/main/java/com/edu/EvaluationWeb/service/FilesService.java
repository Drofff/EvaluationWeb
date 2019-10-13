package com.edu.EvaluationWeb.service;

import com.edu.EvaluationWeb.entity.User;
import com.edu.EvaluationWeb.enums.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class FilesService {

    private final Environment environment;

    public static final String IMAGE_PREFIX = "data:image/png;base64, ";

    @Autowired
    public FilesService(Environment environment) {
        this.environment = environment;
    }

    public String save(MultipartFile multipartFile, FileType fileType, User user) {

        Path dir = Paths.get(fileType.getBasePath(environment));

        try {

            if (Files.notExists(dir)) {
                Files.createDirectory(dir);
            }

            String fileExtension = multipartFile.getName();
            String[] nameParts = fileExtension.split("\\.");
            String fileName = user.getUsername() + "." + nameParts[nameParts.length - 1];

            Path filePath = Paths.get(dir.toString(), fileName);

            if(Files.exists(filePath)) {
                Files.delete(filePath);
            }

            Files.copy(multipartFile.getInputStream(), filePath);

            return fileName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String loadPhoto(String fileName) {
        Path filePath = Paths.get(FileType.PHOTO.getBasePath(environment), fileName);
        if(Files.notExists(filePath)) {
            return null;
        }
        try {
            String encodedPhoto = new String(Base64.getEncoder().encode(Files.readAllBytes(filePath)));
            return IMAGE_PREFIX + encodedPhoto;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
