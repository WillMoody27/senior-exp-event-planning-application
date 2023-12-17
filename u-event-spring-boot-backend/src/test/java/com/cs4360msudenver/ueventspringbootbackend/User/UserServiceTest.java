package com.cs4360msudenver.ueventspringbootbackend.User;

import com.cs4360msudenver.ueventspringbootbackend.Event.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserService.class)
public class UserServiceTest {

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private EntityManager entityManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService.entityManager = entityManager;
    }

    @Test
    public void testGetUsers() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setUserId(userId);
        expectedUser.setFirstName("testFirstName");
        expectedUser.setLastName("testLastName");

        when(userRepository.findAll()).thenReturn(List.of(expectedUser));

        List<User> users = userService.getUsers();
        assertEquals(1, users.size());
        assertEquals("testFirstName", users.get(0).getFirstName());
    }

    @Test
    public void testGetUser() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setUserId(userId);
        expectedUser.setFirstName("testFirstName");
        expectedUser.setLastName("testLastName");

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUser(userId);

        assertNotNull(actualUser);
        assertEquals(expectedUser.getUserId(), actualUser.getUserId());
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
    }

    @Test
    public void testGetUserNotFound() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setUserId(userId);
        expectedUser.setFirstName("testFirstName");
        expectedUser.setLastName("testLastName");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User actualUser = userService.getUser(userId);

        assertNull(actualUser);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "test@example.com";
        User expectedUser = new User();
        expectedUser.setUserId(1L);
        expectedUser.setUsername(email);
        expectedUser.setFirstName("testFirstName");
        expectedUser.setLastName("testLastName");

        when(userRepository.findByUsername(email)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUserByEmail(email);

        assertNotNull(actualUser);
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
    }

    @Test
    public void testGetUserByEmailNotFound() {
        String email = "test@example.com";
        User expectedUser = new User();
        expectedUser.setUserId(1L);
        expectedUser.setUsername(email);
        expectedUser.setFirstName("testFirstName");
        expectedUser.setLastName("testLastName");

        when(userRepository.findByUsername(email)).thenReturn(Optional.empty());

        User actualUser = userService.getUserByEmail(email);

        assertNull(actualUser);
    }

    @Test
    public void testSaveUser() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setUserId(userId);
        expectedUser.setFirstName("testFirstName");
        expectedUser.setLastName("testLastName");

        when(userRepository.saveAndFlush(Mockito.any())).thenReturn(expectedUser);
        when(userRepository.save(Mockito.any())).thenReturn(expectedUser);

        assertEquals(expectedUser, userService.saveUser(expectedUser));
    }

    @Test
    public void testSaveUserError() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setUserId(userId);
        expectedUser.setFirstName("testFirstName");
        expectedUser.setLastName("testLastName");

        when(userRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        when(userRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.saveUser(expectedUser));

        assertNull(exception.getMessage());
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setUserId(userId);
        expectedUser.setFirstName("testFirstName");
        expectedUser.setLastName("testLastName");


        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        boolean isDeleted = userService.deleteUser(userId);

        assertTrue(isDeleted);
        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testDeleteUserNotFound() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setUserId(userId);
        expectedUser.setFirstName("testFirstName");
        expectedUser.setLastName("testLastName");
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(userRepository.existsById(Mockito.any())).thenReturn(false);
        Mockito.doThrow(IllegalArgumentException.class)
                .when(userRepository)
                .deleteById(Mockito.any());

        assertFalse(userService.deleteUser(1L));
    }

    @Test
    public void testDeleteUserThrowsException() {
        Long userId = 1L;
        doThrow(new IllegalArgumentException()).when(userRepository).deleteById(userId);

        when(userRepository.existsById(userId)).thenReturn(true);
        boolean result = userService.deleteUser(userId);
        assertFalse(result);
        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testCreateUserEncodesPasswordAndGeneratesToken() {
        User newUser = new User("testUser", "password", ProviderEnum.LOCAL);
        String encodedPassword = "encodedPassword";
        String token = "token";

        when(passwordEncoder.encode(newUser.getPassword())).thenReturn(encodedPassword);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn(token);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = userService.createUser(newUser);

        assertEquals(encodedPassword, createdUser.getPassword(), "Password should be encoded");
        assertEquals(token, createdUser.getToken(), "Token should be generated and set");

        verify(userRepository, times(1)).save(createdUser);
        assertNotEquals("password", createdUser.getPassword(), "Saved user password should be encoded, not raw");
    }

    @Test
    public void testCreateUserHandlesException() {
        User newUser = new User("testUser", "password", ProviderEnum.LOCAL);
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode(newUser.getPassword())).thenReturn(encodedPassword);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("token");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(RuntimeException.class, () -> userService.createUser(newUser));
        verify(userRepository, times(1)).save(any(User.class));
    }


}
