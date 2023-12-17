package com.cs4360msudenver.ueventspringbootbackend.image;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/images")
public class ImageController {


    @Autowired
    private ImageService imageService;

    @GetMapping
    public ResponseEntity<?> getAllImages() {
        return ResponseEntity.ok(imageService.getAllImages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImageById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(imageService.getImageById(id));
    }

    @PostMapping("/uploadImage/{username}")
    public ResponseEntity<String> saveProduct(@RequestParam("file") MultipartFile file, @PathVariable("username") String username) {
        try {
            imageService.saveImageToDB(file, username);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (Exception e) {
            // Handle the exception and return an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }

    // Custom controller method to get an image by username
    @GetMapping("/image/{username}")
    public ResponseEntity<List<Long>> getImageIdByUsername(@PathVariable("username") String username) {
        List<Long> imageIds = imageService.findIdsByUsername(username);
        if (imageIds.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            return ResponseEntity.ok(Collections.singletonList(imageIds.get(0)));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


        //    Delete Image
        @DeleteMapping("/deleteImage/{id}")
        public ResponseEntity<HttpStatus> deleteImage (@PathVariable("id") Long id){

            try {
                if (imageService.deleteImage(id)) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
            }
        }
    }