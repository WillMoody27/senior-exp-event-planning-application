package com.cs4360msudenver.ueventspringbootbackend.image;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomImageUploadExceptionTest {

    @Test
    void testExceptionMessage() {
        String expectedMessage = "Test message";
        CustomImageUploadException exception = new CustomImageUploadException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}
