package com.cs4360msudenver.ueventspringbootbackend.User;

import com.cs4360msudenver.ueventspringbootbackend.Event.EventService;
import com.cs4360msudenver.ueventspringbootbackend.Interests.InterestService;
import com.cs4360msudenver.ueventspringbootbackend.image.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class JwtRequestFilterTest {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private EventService eventService;

    @MockBean
    private UserService userService;

    @MockBean
    private InterestService interestService;

    @MockBean
    private ImageService imageService;

    @Test
    public void whenValidJwtProvided_thenAuthenticateUser() throws ServletException, IOException {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        String jwt = "valid.jwt.token";
        String username = "testUser";

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        Mockito.when(jwtUtil.extractUsername(jwt)).thenReturn(username);

        UserDetails mockUserDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);
        Mockito.when(jwtUtil.validateToken(jwt, mockUserDetails)).thenReturn(true);

        // Act
        jwtRequestFilter.doFilterInternal(request, response, chain);

        // Assert
        Mockito.verify(chain).doFilter(request, response);
        // Additional assertions can be made here
    }
}

