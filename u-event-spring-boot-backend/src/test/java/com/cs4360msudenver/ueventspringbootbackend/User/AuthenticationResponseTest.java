package com.cs4360msudenver.ueventspringbootbackend.User;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class AuthenticationResponseTest {

    @Test
    public void testConstructorAndGetJwt() {
        String jwt = "testJwtToken";
        AuthenticationResponse response = new AuthenticationResponse(jwt);

        assertThat(response).isNotNull();
        assertThat(response.getJwt()).isEqualTo(jwt);
    }
}