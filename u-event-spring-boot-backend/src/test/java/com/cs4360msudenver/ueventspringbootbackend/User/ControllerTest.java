//package com.cs4360msudenver.ueventspringbootbackend.UserController;
//
//import User.User;
//import User.UserRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class ControllerTest {
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserController userController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateUser() {
//        User user = new User();
//        when(userRepository.save(user)).thenReturn(user);
//        ResponseEntity<String> response = userController.createUser(user);
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals("User Created", response.getBody());
//        verify(userRepository, times(1)).save(user);
//
//
//    }
//
//    @Test
//    void testGetUsers() {
//        List<User> users = new ArrayList<>();
//        when(userRepository.findAll()).thenReturn(users);
//        ResponseEntity<Iterable<User>> response = userController.getUsers();
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(users, response.getBody());
//        verify(userRepository, times(1)).findAll();
//
//    }
//
//    @Test
//    void testGetUser() {
//        Long userId = 1L;
//        User user = new User();
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        ResponseEntity<Object> response = userController.getUser(userId);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user, response.getBody());
//        verify(userRepository, times(1)).findById(userId);
//
//    }
//
//    @Test
//    void testGetUserNotFound() {
//        Long userId = 2L;
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//        ResponseEntity<Object> response = userController.getUser(userId);
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertNull(response.getBody());
//        verify(userRepository, times(1)).findById(userId);
//    }
//}


//    @Test
//    void TestUserUpdated() {
//        // Arrange
//        Long userId = 1L;
//        User originalUser = new User();
//        originalUser.setUsername("OriginalUsername");
//        originalUser.setFirstName("OriginalFirstName");
//        originalUser.setLastName("OriginalLastName");
//        originalUser.setEmail("original@example.com");
//        originalUser.setPassword("OriginalPassword");
//
//        User updatedUser = new User();
//        updatedUser.setUsername("UpdatedUsername");
//        updatedUser.setFirstName("UpdatedFirstName");
//        updatedUser.setLastName("UpdatedLastName");
//        updatedUser.setEmail("updated@example.com");
//        updatedUser.setPassword("UpdatedPassword");
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(originalUser));
//        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
//
//        // Act
//        ResponseEntity<Object> response = userController.updateUser(userId, updatedUser);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("User Updated", response.getBody());
//
//        assertNull("UpdatedUsername", originalUser.getUsername());
//        assertEquals("UpdatedFirstName", originalUser.getFirstName());
//        assertEquals("UpdatedLastName", originalUser.getLastName());
//        assertEquals("updated@example.com", originalUser.getEmail());
//        assertEquals("UpdatedPassword", originalUser.getPassword());
//
//        verify(userRepository, times(1)).findById(userId);
//        verify(userRepository, times(1)).save(originalUser);
//    }
//
//
//
//
//
//
//        void testDeleteUser() {
//        Long userId = 1L;
//        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
//        ResponseEntity<Object> response = userController.deleteUser(userId);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("User Deleted", response.getBody());
//
//        verify(userRepository, times(1)).findById(userId);
//        verify(userRepository, times(1)).deleteById(userId);
//    }
//
//
//}
