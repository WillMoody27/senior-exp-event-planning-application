package com.cs4360msudenver.ueventspringbootbackend.image;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;


@Service
public class ImageService {

    private final ImageRepository imageRepository;
    public EntityManager entityManager;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }


    public void saveImageToDB(MultipartFile file, String username) throws CustomImageUploadException {
        try {
            // Check if an image with the same username already exists
            Optional<Image> existingImage = imageRepository.findByUsername(username);

            Image img;

            if (existingImage.isPresent()) {
                // If it exists, get that image object to update
                img = existingImage.get();
            } else {
                // Else, create a new one
                img = new Image();
                img.setUsername(username);
            }

            // Have to retrieve the file name from actual file name using StringUtils.cleanPath
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileName.contains("..")) {
                System.out.println("Not a valid file");
                return; // Exit the function if the file is not valid
            }

            // Transform and encode the image to its string representation using Base64
            img.setImage(Base64.getEncoder().encodeToString(file.getBytes()));

            // Save (or update) the image in the database
            imageRepository.save(img);
        } catch (IOException e) {
            throw new CustomImageUploadException("Failed to store image");

        }
    }
//

    // Service method to get an image by username

    // Service method to get an image id by username
//    public Long findIdByUsername(String username) {
//        return imageRepository.findIdByUsername(username).orElse(null);
//    }

    public List<Long> findIdsByUsername(String username) {
        return imageRepository.findIdsByUsername(username);
    }


    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public Image getImageById(Long id) {
        try {
            return imageRepository.findById(id).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean deleteImage(Long id) {
        try {
            if (imageRepository.existsById(id)) {
                imageRepository.deleteById(id);
                return true;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }
}