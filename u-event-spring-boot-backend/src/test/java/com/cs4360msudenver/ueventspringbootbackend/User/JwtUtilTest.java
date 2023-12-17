package com.cs4360msudenver.ueventspringbootbackend.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

//This unit test was created with assistance from OpenAI's GPT-4 model.
public class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String SECRET_KEY = "your-256-bit-secret"; // Use the same secret key as in your application

    @BeforeEach
    public void setup() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET_KEY);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationInMs", 3600000); // 1 hour for example
    }

    @Test
    public void testExtractUsername() {
        // Create a UserDetails object with a username
        UserDetails userDetails = new User("testUser", "password", new ArrayList<>());

        // Generate a token using the UserDetails object
        String token = jwtUtil.generateToken(userDetails);

        // Extract the username from the token
        String extractedUsername = jwtUtil.extractUsername(token);

        // Assert that the extracted username matches the one in the UserDetails object
        assertEquals(userDetails.getUsername(), extractedUsername);
    }
}

