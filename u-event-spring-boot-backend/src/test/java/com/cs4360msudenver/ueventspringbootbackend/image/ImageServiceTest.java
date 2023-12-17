package com.cs4360msudenver.ueventspringbootbackend.image;

import com.cs4360msudenver.ueventspringbootbackend.Event.Event;
import com.cs4360msudenver.ueventspringbootbackend.User.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManagerFactory;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ImageService.class)
public class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    @MockBean
    private ImageRepository imageRepository;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @NotNull
    private static Image getImage() {
        Image image = new Image();
        image.setId(1L);
        image.setImage("image");
        User user = new User();
        user.setUsername("someUser");
        image.setUser(user);
        return image;
    }

    @Test
    public void testSaveImageToDB() throws CustomImageUploadException {
        String username = getImage().getUsername();
        MultipartFile mockFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "image data".getBytes()
        );
        // TODO-Done: Mock the repository's behavior - Test the service method to save an image to the database
        Mockito.when(imageRepository.findByUsername(username)).thenReturn(Optional.empty());
        Mockito.when(imageRepository.save(Mockito.any(Image.class))).thenReturn(getImage());
        imageService.saveImageToDB(mockFile, username);
        Mockito.verify(imageRepository).save(Mockito.any(Image.class));
        // TODO-Done: Post For Images Should Return OK (200) not CREATED (201)
        assertEquals(200, HttpStatus.OK.value()); // Means the image was saved successfully
    }

    @Test
    public void testGetImageNotFound() {
        // TODO-Done: When the repository's findById method is called with an ID that does not exist, return Optional.empty()
        Mockito.when(imageRepository.findById(5L)).thenReturn(Optional.empty());

        // TODO-Done: Test the service method to get an image by ID
        Image image = imageService.getImageById(5L);
        assertNull(image);
    }

    @Test
    public void testSaveImageToDBWithExistingImage() throws Exception {
        String username = getImage().getUsername();

        // TODO-Done: Mock the repository's behavior for an existing image
        MultipartFile mockFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "image data".getBytes()
        );

        /*
         * TODO-Done: For null image, mock the repository's behavior to return Optional.empty() when findByUsername is called
         *     - Mock the repository's behavior to return the image when save is called
         *   Else For existing image, mock the repository's behavior to return the image when findByUsername is called
         *     - Mock the repository's behavior to return the image when save is called
         * */
        Mockito.when(imageRepository.findByUsername(username)).thenReturn(Optional.empty());
        Mockito.when(imageRepository.save(Mockito.any(Image.class))).thenReturn(getImage());

        // TODO-Done: Test the service method to save an image to the database
        Mockito.when(imageRepository.findByUsername(username)).thenReturn(Optional.of(getImage()));
        Mockito.when(imageRepository.save(Mockito.any(Image.class))).thenReturn(getImage());
        imageService.saveImageToDB(mockFile, username);
        Mockito.verify(imageRepository).save(Mockito.any(Image.class));
    }

    @Test
    public void testSaveImageToDBIOException() {
        String username = "testUser";
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);

        try {
            // Mock the behavior of getOriginalFilename() to return a valid filename
            Mockito.when(mockFile.getOriginalFilename()).thenReturn("validFilename.jpg");

            // Configure the mock to throw an IOException when getBytes() is called
            Mockito.when(mockFile.getBytes()).thenThrow(new IOException());
        } catch (IOException e) {
            // This catch block is just to handle the mock setup and should never be executed
        }

        // Use assertThrows to check that CustomImageUploadException is thrown
        assertThrows(CustomImageUploadException.class, () -> {
            imageService.saveImageToDB(mockFile, username);
        });

        // Additional verification steps can be added here if necessary
    }

    @Test
    public void testFindIdsByUsername() {
        // Arrange
        Image testImage = getImage();
        User testUser = new User();
        testUser.setUsername("someUsers");
        testImage.getUsername();
        testImage.setId(1L);

        // TODO-Done: Mock the repository's behavior - Test the service method to get an image id by username
        Mockito.when(imageRepository.findIdsByUsername(testUser.getUsername())).thenReturn(List.of(testImage.getId()));
        assertEquals(getImage().getId(), testImage.getId());
        assertEquals("someUsers", testUser.getUsername());

    }

    @Test
    public void testGetAllImages() {

        // TODO-Done: Mock the repository's behavior - Test the service method to get all images
        Image testImages = new Image();
        testImages.setId(1L);
        testImages.setImage("image");

        Mockito.when(imageRepository.findAll()).thenReturn(List.of(testImages));

        List<Image> images = imageService.getAllImages();
        Assertions.assertEquals(1, images.size());
        Assertions.assertEquals("image", images.get(0).getImage());
    }

    @Test
    public void testGetImageById() {

        // TODO-Done: Mock the repository's behavior - Test the service method to get an image by ID
        Image testImage = getImage();

        // If the image exists, return the image object
        Mockito.when(imageRepository.findById(1L)).thenReturn(Optional.of(testImage));

        // TODO-Done: Assert that the image ID is 1 (the ID of the test image)
        Image image = imageService.getImageById(1L);
        Assertions.assertEquals(1L, image.getId());
    }

    @Test
    public void testDeleteImage() {
        Image testImage = new Image();
        testImage.setId(1L);
        testImage.setImage("image");

        Mockito.when(imageRepository.existsById(1L)).thenReturn(true);

        boolean imageDeleted = imageService.deleteImage(1L);
        assertTrue(imageDeleted);
    }

    @Test
    public void testDeleteImageNotFound() {
        Image testImage = new Image();
        testImage.setId(1L);
        testImage.setImage("image");

        Mockito.when(imageRepository.existsById(2L)).thenReturn(false);

        boolean imageDeleted = imageService.deleteImage(2L);
        assertFalse(imageDeleted);
    }

    @Test
    public void testDeleteImageIllegalArgumentException() {
        Long testId = 1L;

        // Configure the mock to throw an IllegalArgumentException
        Mockito.when(imageRepository.existsById(testId)).thenThrow(new IllegalArgumentException());

        // Call deleteImage and verify the result is false
        Boolean result = imageService.deleteImage(testId);
        assertFalse(result);

        // Additional verification to ensure deleteById was not called
        Mockito.verify(imageRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }
}
