package com.cs4360msudenver.ueventspringbootbackend.User;

import com.cs4360msudenver.ueventspringbootbackend.Event.Event;
import com.cs4360msudenver.ueventspringbootbackend.Event.EventService;
import com.cs4360msudenver.ueventspringbootbackend.Interests.InterestService;
import com.cs4360msudenver.ueventspringbootbackend.Interests.Interests;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    @MockBean
    private EventService eventService;
    @MockBean
    private InterestService interestService;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private AuthenticationManager authenticationManager;


    @NotNull
    private static User getUser() {
        User testUser = new User();
        testUser.setUsername("testEmail");
        testUser.setFirstName("testFirstName");
        testUser.setLastName("testLastName");
        testUser.setPassword("testPassword");
        testUser.setProvider(ProviderEnum.LOCAL);
        testUser.setRole(User.userRole.USER);
        return testUser;
    }

    @Test
    public void testGetUsers() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        User testUser = getUser();
        User testUser2 = getUser();
        User testUser3 = getUser();

        testUser2.setUsername("testEmail2");
        testUser3.setUsername("testEmail3");

        Mockito.when(userService.getUsers()).thenReturn(List.of(testUser, testUser2, testUser3));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        List<User> users = userService.getUsers();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("testEmail", users.get(0).getUsername());
        assertEquals("testEmail2", users.get(1).getUsername());
        assertEquals("testEmail3", users.get(2).getUsername());
    }

    @Test
    public void testGetUsersBadRequest() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        User testUser = getUser();
        User testUser2 = getUser();
        User testUser3 = getUser();

        testUser2.setUsername("testEmail2");
        testUser3.setUsername("testEmail3");

        Mockito.when(userService.getUsers()).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }
    @Test
    public void testGetUser() throws Exception {
        // Arrange
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/users/testEmail")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        User testUser = getUser();

        // Mock the behavior of userDetailsService to return a UserDetails object with the correct username
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("testEmail")
                .password(testUser.getPassword())
                .authorities("USER") // assuming "USER" is a valid role, adjust if necessary
                .build();

        Mockito.when(customUserDetailsService.loadUserByUsername("testEmail")).thenReturn(userDetails);
        Mockito.when(userService.getUserByEmail("testEmail")).thenReturn(testUser);

        // Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String responseContent = response.getContentAsString();


        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        // you might want to assert the response content here as well
        assertEquals("testEmail", JsonPath.parse(responseContent).read("$.username"));
        assertEquals("testFirstName", JsonPath.parse(responseContent).read("$.firstName"));
        assertEquals("testLastName", JsonPath.parse(responseContent).read("$.lastName"));
        assertEquals("testPassword", JsonPath.parse(responseContent).read("$.password"));
    }


    @Test
    public void testGetUserNotFound() throws Exception {
        // Arrange
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/users/testUsername")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        User testUser = getUser();

        // Mock the behavior of userDetailsService to return a UserDetails object with the correct username
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("testUsername")
                .password(testUser.getPassword())
                .authorities("USER") // assuming "USER" is a valid role, adjust if necessary
                .build();

        Mockito.when(customUserDetailsService.loadUserByUsername("testUsername")).thenReturn(userDetails);
        Mockito.when(userService.getUserByEmail("testUsername")).thenReturn(null);

        // Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testGetUserBadRequest() throws Exception {
        // Arrange
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/users/testUsername")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        User testUser = getUser();

        // Mock the behavior of userDetailsService to return a UserDetails object with the correct username
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("testUsername")
                .password(testUser.getPassword())
                .authorities("USER") // assuming "USER" is a valid role, adjust if necessary
                .build();

        Mockito.when(customUserDetailsService.loadUserByUsername("testUsername")).thenReturn(userDetails);
        Mockito.when(userService.getUserByEmail("testUsername")).thenThrow(IllegalArgumentException.class);

        // Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

    @Test
    public void testCreateUser() throws Exception {
        // Create a mock request to create a user
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"username\":\"testEmail\"," +
                        "\"firstName\":\"testFirstName\"," +
                        "\"lastName\":\"testLastName\"," +
                        "\"password\":\"testPassword\"," +
                        "\"provider\":\"LOCAL\"," +
                        "\"phoneNumber\":\"0000000000\"," +
                        "\"role\":\"USER\"," +
                        "\"jobDescription\":\"testJobDescription\"," +
                        "\"nickname\":\"testNickname\"," +
                        "\"address\":\"testAddress\"," +
                        "\"postalCode\":\"testPostalCode\"" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        User testUser = getUser();

        // Mock the behavior of userService to return the testUser object
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(testUser);

        // Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        String responseContent = response.getContentAsString();
        System.out.println("Response content: " + responseContent);

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("testEmail", JsonPath.parse(responseContent).read("$.username"));
    }

    @Test
    public void testCreateUserBadRequest() throws Exception {
        // Create a mock request to create a user
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"username\":\"testEmail\"," +
                        "\"firstName\":\"testFirstName\"," +
                        "\"lastName\":\"testLastName\"," +
                        "\"password\":\"testPassword\"," +
                        "\"provider\":\"LOCAL\"," +
                        "\"phoneNumber\":\"0000000000\"," +
                        "\"role\":\"USER\"," +
                        "\"jobDescription\":\"testJobDescription\"," +
                        "\"nickname\":\"testNickname\"," +
                        "\"address\":\"testAddress\"," +
                        "\"postalCode\":\"testPostalCode\"" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        User testUser = getUser();

        // Mock the behavior of userService to return the testUser object
        Mockito.when(userService.createUser(Mockito.any())).thenThrow(IllegalArgumentException.class);

        // Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        String responseContent = response.getContentAsString();
        System.out.println("Response content: " + responseContent);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Arrange
        String username = "testEmail";
        User originalUser = getUser();
        User updatedUser = getUser();
        updatedUser.setFirstName("updatedFirstName");
        updatedUser.setLastName("updatedLastName");

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(originalUser.getPassword())
                .authorities("USER") // assuming "USER" is a valid role, adjust if necessary
                .build();

        Mockito.when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        Mockito.when(userService.getUserByEmail(username)).thenReturn(originalUser);
        Mockito.when(userService.saveUser(Mockito.any())).thenReturn(updatedUser);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/users/" + username)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"updatedFirstName\",\"lastName\":\"updatedLastName\"}")
                .contentType(MediaType.APPLICATION_JSON);

        // Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String responseContent = response.getContentAsString();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("updatedFirstName", JsonPath.parse(responseContent).read("$.firstName"));
        assertEquals("updatedLastName", JsonPath.parse(responseContent).read("$.lastName"));
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        // Arrange
        String username = "testEmail";
        User originalUser = getUser();
        User updatedUser = getUser();
        updatedUser.setFirstName("updatedFirstName");
        updatedUser.setLastName("updatedLastName");

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(originalUser.getPassword())
                .authorities("USER") // assuming "USER" is a valid role, adjust if necessary
                .build();

        Mockito.when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        Mockito.when(userService.getUserByEmail(username)).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/users/" + username)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"updatedFirstName\",\"lastName\":\"updatedLastName\"}")
                .contentType(MediaType.APPLICATION_JSON);

        // Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateUserBadRequest() throws Exception {
        // Arrange
        String username = "testEmail";
        User originalUser = getUser();
        User updatedUser = getUser();
        updatedUser.setFirstName("updatedFirstName");
        updatedUser.setLastName("updatedLastName");

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(originalUser.getPassword())
                .authorities("USER") // assuming "USER" is a valid role, adjust if necessary
                .build();

        Mockito.when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        Mockito.when(userService.getUserByEmail(username)).thenReturn(originalUser);
        Mockito.when(userService.saveUser(Mockito.any())).thenThrow(IllegalArgumentException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/users/" + username)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"updatedFirstName\",\"lastName\":\"updatedLastName\"}")
                .contentType(MediaType.APPLICATION_JSON);

        // Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Arrange
        String username = "testEmail";
        User userToDelete = getUser();

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(userToDelete.getPassword())
                .authorities("USER") // assuming "USER" is a valid role, adjust if necessary
                .build();

        Mockito.when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        Mockito.when(userService.getUserByEmail(username)).thenReturn(userToDelete);
        Mockito.when(userService.deleteUser(userToDelete.getUserId())).thenReturn(true);

        RequestBuilder requestBuilder = delete("/api/users/" + username)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        // Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }



    @Test
    public void testDeleteUserNotFound() throws Exception {
        // Arrange
        String username = "testEmail";
        User userToDelete = getUser();

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(userToDelete.getPassword())
                .authorities("USER") // assuming "USER" is a valid role, adjust if necessary
                .build();

        Mockito.when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        Mockito.when(userService.getUserByEmail(username)).thenReturn(userToDelete);
        Mockito.when(userService.deleteUser(userToDelete.getUserId())).thenReturn(false);

        RequestBuilder requestBuilder = delete("/api/users/" + username)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        // Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void testDeleteUserBadRequest() throws Exception {
        // Arrange
        String username = "testEmail";
        User userToDelete = getUser();

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(userToDelete.getPassword())
                .authorities("USER") // assuming "USER" is a valid role, adjust if necessary
                .build();

        Mockito.when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        Mockito.when(userService.getUserByEmail(username)).thenReturn(userToDelete);
        Mockito.when(userService.deleteUser(userToDelete.getUserId())).thenThrow(IllegalArgumentException.class);

        RequestBuilder requestBuilder = delete("/api/users/" + username)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        // Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    //This test was written with assistance from OpenAI's GPT-4 model
    @Test
    public void testGenerateToken() throws Exception {
        AuthenticationRequest authRequest = new AuthenticationRequest("testuser", "password", "testJwtToken");

        Authentication authentication = mock(Authentication.class);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(authRequest.getUsername());

        given(authenticationManager.authenticate(org.mockito.ArgumentMatchers.any(Authentication.class))).willReturn(authentication);
        given(customUserDetailsService.loadUserByUsername(authRequest.getUsername())).willReturn(userDetails);
        given(jwtUtil.generateToken(userDetails)).willReturn("testJwtToken");

        MvcResult mvcResult = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()))
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualTo("testJwtToken");

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        verify(customUserDetailsService).loadUserByUsername(authRequest.getUsername());
        verify(jwtUtil).generateToken(userDetails);
    }

    //This test was written with assistance from OpenAI's GPT-4 model
    @Test
    public void testSignupForEvent() throws Exception {
        String username = "testuser";
        Long eventId = 1L;

        UserDetails mockUserDetails = mock(UserDetails.class);
        User mockUser = mock(User.class);
        Event mockEvent = mock(Event.class);

        when(mockUserDetails.getUsername()).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);
        when(userService.getUserByEmail(username)).thenReturn(mockUser);
        when(eventService.getEvent(eventId)).thenReturn(mockEvent);

        // Assuming we have setters or methods to add to the events and attendees list
        when(mockUser.getUsername()).thenReturn(username);
        when(mockEvent.getEventName()).thenReturn("Test Event");

        // Perform the actual test action
        mockMvc.perform(post("/api/users/{username}/events/{eventId}", username, eventId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("signed up for event")));

        // Verify the interactions
        verify(userService).saveUser(any(User.class));
        verify(eventService).saveEvent(any(Event.class));
    }

    //This test was written with assistance from OpenAI's GPT-4 model
    @Test
    public void testSignupForEventBadRequest() throws Exception {
        String username = "testuser";
        Long eventId = 1L;

        UserDetails mockUserDetails = mock(UserDetails.class);
        User mockUser = mock(User.class);
        Event mockEvent = mock(Event.class);

        Set<Event> events = new HashSet<>();
        when(mockUser.getEvents()).thenReturn(events);

        when(mockUserDetails.getUsername()).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);
        when(userService.getUserByEmail(username)).thenReturn(mockUser);
        when(eventService.getEvent(eventId)).thenReturn(mockEvent);

        doThrow(new RuntimeException("Failed to save user or event")).when(userService).saveUser(any(User.class));

        mockMvc.perform(post("/api/users/{username}/events/{eventId}", username, eventId))
                .andExpect(status().isBadRequest());

        verify(userService).saveUser(any(User.class));
        verify(eventService, never()).saveEvent(any(Event.class));
    }

    //This test was written with assistance from OpenAI's GPT-4 model
    @Test
    public void testSelectedInterests() throws Exception {
        String username = "testuser";
        String interestName = "Hiking";

        // Mock the necessary objects
        UserDetails mockUserDetails = mock(UserDetails.class);
        User mockUser = mock(User.class);
        Interests mockInterest = mock(Interests.class);

        // Set up the expected behavior of the mocks
        when(mockUserDetails.getUsername()).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);
        when(userService.getUserByEmail(username)).thenReturn(mockUser);
        when(interestService.getInterestByName(interestName)).thenReturn(mockInterest);

        // Assuming the mockUser has a method to manage interests
        Set<Interests> interests = new HashSet<>();
        when(mockUser.getInterests()).thenReturn(interests);

        // Perform the test action
        mockMvc.perform(post("/api/users/{username}/interests/{interest}", username, interestName))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("has selected interest")));

        // Verify interactions
        verify(userService).saveUser(any(User.class));
        verify(interestService).saveInterest(any(Interests.class));
    }


    //This test was written with assistance from OpenAI's GPT-4 model
    @Test
    public void testUnJoinForEvent() throws Exception {
        String username = "testuser";
        Long eventId = 1L;

        // Mock user details and user
        UserDetails mockUserDetails = mock(UserDetails.class);
        User mockUser = mock(User.class);
        Event mockEvent = mock(Event.class);

        // Set up mock behavior
        when(mockUserDetails.getUsername()).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);
        when(userService.getUserByEmail(username)).thenReturn(mockUser);
        when(eventService.getEvent(eventId)).thenReturn(mockEvent);

        // Assuming the mockUser and mockEvent have methods to manage events and attendees
        Set<Event> events = new HashSet<>();
        events.add(mockEvent);
        when(mockUser.getEvents()).thenReturn(events);

        // Perform the test action
        mockMvc.perform(delete("/api/users/{username}/events/{eventId}/unjoin", username, eventId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("left the event")));

        // Verify interactions
        verify(userService).saveUser(any(User.class));
        verify(eventService).saveEvent(any(Event.class));
    }

    //This test was written with assistance from OpenAI's GPT-4 model
    @Test
    public void testUnJoinForEventBadRequest() throws Exception {
        String username = "testuser";
        Long eventId = 1L;

        // Mock user details, user, and event
        UserDetails mockUserDetails = mock(UserDetails.class);
        User mockUser = mock(User.class);
        Event mockEvent = mock(Event.class);

        // Set up mock behavior
        when(mockUserDetails.getUsername()).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);
        when(userService.getUserByEmail(username)).thenReturn(mockUser);
        when(eventService.getEvent(eventId)).thenReturn(mockEvent);

        // Assuming the mockUser has methods to manage events
        Set<Event> events = new HashSet<>();
        events.add(mockEvent);
        when(mockUser.getEvents()).thenReturn(events);

        // Simulate an exception during the unjoin process
        doThrow(new RuntimeException("Failed to unjoin event")).when(userService).saveUser(any(User.class));

        // Perform the test action and expect a BadRequest response
        mockMvc.perform(delete("/api/users/{username}/events/{eventId}/unjoin", username, eventId))
                .andExpect(status().isBadRequest());

        // Verify that the saveUser method was called, which caused the exception
        verify(userService).saveUser(any(User.class));

        verify(eventService, never()).saveEvent(any(Event.class));
    }

    //This test was written with assistance from OpenAI's GPT-4 model
    @Test
    public void testDeleteUserInterest() throws Exception {
        String username = "testuser";
        Long interestId = 1L;

        // Mock the necessary objects
        UserDetails mockUserDetails = mock(UserDetails.class);
        User mockUser = mock(User.class);
        Interests mockInterest = mock(Interests.class);

        // Set up the expected behavior of the mocks
        when(mockUserDetails.getUsername()).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);
        when(userService.getUserByEmail(username)).thenReturn(mockUser);
        when(interestService.getInterest(interestId)).thenReturn(mockInterest);

        // Assuming the mockUser has a method to manage interests
        Set<Interests> interests = new HashSet<>();
        interests.add(mockInterest);
        when(mockUser.getInterests()).thenReturn(interests);

        // Perform the test action
        mockMvc.perform(delete("/api/users/{username}/interests/{id}", username, interestId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("has removed interest")));

        // Verify interactions
        verify(userService).saveUser(any(User.class));
        verify(interestService).saveInterest(any(Interests.class));
    }

    //This test was written with assistance from OpenAI's GPT-4 model
    @Test
    public void testDeleteUserInterestBadRequest() throws Exception {
        String username = "testuser";
        Long interestId = 1L;

        // Mock the necessary objects
        UserDetails mockUserDetails = mock(UserDetails.class);
        User mockUser = mock(User.class);
        Interests mockInterest = mock(Interests.class);

        // Set up the expected behavior of the mocks
        when(mockUserDetails.getUsername()).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);
        when(userService.getUserByEmail(username)).thenReturn(mockUser);
        when(interestService.getInterest(interestId)).thenReturn(mockInterest);

        // Assuming the mockUser has a method to manage interests
        Set<Interests> interests = new HashSet<>();
        interests.add(mockInterest);
        when(mockUser.getInterests()).thenReturn(interests);

        // Simulate an exception during the interest removal process
        doThrow(new RuntimeException("Failed to remove user interest")).when(userService).saveUser(any(User.class));

        // Perform the test action and expect a BadRequest response
        mockMvc.perform(delete("/api/users/{username}/interests/{id}", username, interestId))
                .andExpect(status().isBadRequest());

        // Verify that the saveUser method was called, which caused the exception
        verify(userService).saveUser(any(User.class));

        verify(interestService, never()).saveInterest(any(Interests.class));
    }
}