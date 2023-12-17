package com.cs4360msudenver.ueventspringbootbackend.User;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AuthenticationRequestTest {

    @Test
    public void testDefaultConstructor() {
        AuthenticationRequest request = new AuthenticationRequest();
        assertThat(request).isNotNull();
        assertThat(request.getUsername()).isNull();
        assertThat(request.getPassword()).isNull();
    }

    @Test
    public void testParameterizedConstructor() {
        AuthenticationRequest request = new AuthenticationRequest("testUsername", "testPassword", "testJwtToken");
        assertThat(request).isNotNull();
        assertThat(request.getUsername()).isEqualTo("testUsername");
        assertThat(request.getPassword()).isEqualTo("testPassword");
    }

    @Test
    public void testGettersAndSetters() {
        AuthenticationRequest request = new AuthenticationRequest();

        request.setUsername("testUsername");
        request.setPassword("testPassword");

        assertThat(request.getUsername()).isEqualTo("testUsername");
        assertThat(request.getPassword()).isEqualTo("testPassword");
    }
}

