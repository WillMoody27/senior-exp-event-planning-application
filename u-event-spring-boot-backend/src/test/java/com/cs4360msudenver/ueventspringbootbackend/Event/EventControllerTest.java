package com.cs4360msudenver.ueventspringbootbackend.Event;

import com.cs4360msudenver.ueventspringbootbackend.User.CustomUserDetailsService;
import com.cs4360msudenver.ueventspringbootbackend.User.JwtUtil;
import com.cs4360msudenver.ueventspringbootbackend.User.UserService;
import com.jayway.jsonpath.JsonPath;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private UserService userService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtUtil jwtUtil;


    @NotNull
    private static Event getEvent() throws ParseException {
        Event testEvent = new Event();
        testEvent.setEventName("testEvent");
        testEvent.setUsername("testEmail");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse("2021-09-27");
        testEvent.setEventDate(date);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss"); // Use the appropriate time format
        Time time = new Time(timeFormat.parse("12:00:00").getTime()); // Replace "12:00:00" with the actual time string
        testEvent.setEventTime(time.toLocalTime());
        testEvent.setLocation("testLocation");
        testEvent.setPostalCode("testPostalCode");
        testEvent.setCity("testCity");
        testEvent.setCountryCode("testCountry");
        testEvent.setDescription("testDescription");
        testEvent.setCategory("testCategory");
        testEvent.setImage("testImage");
        testEvent.setImageFile(null);
        testEvent.setTags(null);
        testEvent.setAttendees(null);
        return testEvent;
    }

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testGetEvents() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/events/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Event testEvent = getEvent();

        Mockito.when(eventService.getEvents()).thenReturn(List.of(testEvent));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        List<Event> events = eventService.getEvents();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("testEvent", events
                .get(0)
                .getEventName());
    }

    @Test
    public void testGetEvent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/events/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Event testEvent = getEvent();

        Mockito.when(eventService.getEvent(1L)).thenReturn(testEvent);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        String responseContent = response.getContentAsString();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        // Using JSONPath to assert the individual fields
        assertEquals("testEvent", JsonPath.parse(responseContent).read("$.eventName"));
        assertEquals("2021-09-27T06:00:00.000+00:00", JsonPath.parse(responseContent).read("$.eventDate"));
        assertEquals("12:00:00", JsonPath.parse(responseContent).read("$.eventTime"));
        assertEquals("testLocation", JsonPath.parse(responseContent).read("$.location"));
        assertEquals("testPostalCode", JsonPath.parse(responseContent).read("$.postalCode"));
        assertEquals("testCity", JsonPath.parse(responseContent).read("$.city"));
        assertEquals("testCountry", JsonPath.parse(responseContent).read("$.countryCode"));
        assertEquals("testDescription", JsonPath.parse(responseContent).read("$.description"));
        assertEquals("testCategory", JsonPath.parse(responseContent).read("$.category"));
        assertEquals("testImage", JsonPath.parse(responseContent).read("$.image"));
        assertNull(JsonPath.parse(responseContent).read("$.imageFile"));
        assertNull(JsonPath.parse(responseContent).read("$.tags"));
    }

    @Test
    public void testGetEventNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/events/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(eventService.getEvent(1L)).thenReturn(null);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testGetEventsException() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/events/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        // Configure the mock to throw an exception
        Mockito.when(eventService.getEvents()).thenThrow(new RuntimeException("Test Exception"));

        // Perform the request
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        // Get the response
        MockHttpServletResponse response = result.getResponse();

        // Assert the response status and content
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        // You can also check the response content if it contains a stack trace or a specific error message
    }

    @Test
    public void testGetEventException() throws Exception {
        Long testId = 1L; // Example ID
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/events/" + testId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        // Configure the mock to throw an exception
        Mockito.when(eventService.getEvent(testId)).thenThrow(new RuntimeException("Test Exception"));

        // Perform the request
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        // Get the response
        MockHttpServletResponse response = result.getResponse();

        // Assert the response status and content
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        // You can also check the response content if it contains a stack trace or a specific error message
    }

    @Test
    public void testCreateEvent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/events/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"eventName\": \"testEvent\",\n" +
                        "    \"username\": \"testEmail\",\n" +
                        "    \"eventDate\": \"2021-09-27\",\n" +
                        "    \"eventTime\": \"12:00:00\",\n" +
                        "    \"location\": \"testLocation\",\n" +
                        "    \"postalCode\": \"testPostalCode\",\n" +
                        "    \"countryCode\": \"testCountry\",\n" +
                        "    \"description\": \"testDescription\",\n" +
                        "    \"category\": \"testCategory\",\n" +
                        "    \"image\": \"testImage\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        Event testEvent = getEvent();

        Mockito.when(eventService.saveEvent(Mockito.any())).thenReturn(testEvent);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        String responseContent = response.getContentAsString();

        // Using JSONPath to assert the individual fields
        assertEquals("testEvent", JsonPath.parse(responseContent).read("$.eventName"));
        assertEquals("2021-09-27T06:00:00.000+00:00", JsonPath.parse(responseContent).read("$.eventDate"));
        assertEquals("12:00:00", JsonPath.parse(responseContent).read("$.eventTime"));
        assertEquals("testLocation", JsonPath.parse(responseContent).read("$.location"));
        assertEquals("testPostalCode", JsonPath.parse(responseContent).read("$.postalCode"));
        assertEquals("testCity", JsonPath.parse(responseContent).read("$.city"));
        assertEquals("testCountry", JsonPath.parse(responseContent).read("$.countryCode"));
        assertEquals("testDescription", JsonPath.parse(responseContent).read("$.description"));
        assertEquals("testCategory", JsonPath.parse(responseContent).read("$.category"));
        assertEquals("testImage", JsonPath.parse(responseContent).read("$.image"));
        assertNull(JsonPath.parse(responseContent).read("$.imageFile"));
        assertNull(JsonPath.parse(responseContent).read("$.tags"));
    }

    @Test
    public void testCreateCountryBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/events/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"eventName\": \"testEvent\",\n" +
                        "    \"username\": \"testEmail\",\n" +
                        "    \"eventDate\": \"2021-09-27\",\n" +
                        "    \"eventTime\": \"12:00:00\",\n" +
                        "    \"location\": \"testLocation\",\n" +
                        "    \"postalCode\": \"testPostalCode\",\n" +
                        "    \"countryCode\": \"testCountry\",\n" +
                        "    \"description\": \"testDescription\",\n" +
                        "    \"category\": \"testCategory\",\n" +
                        "    \"image\": \"testImage\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        Event testEvent = getEvent();

        Mockito.when(eventService.saveEvent(Mockito.any())).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));

    }

    @Test
    public void testUpdateEvent() throws Exception {
        // Build the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/events/update/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"eventName\": \"testEvent\",\n" +
                        "    \"username\": \"testEmail\",\n" +
                        "    \"eventDate\": \"2021-09-27\",\n" +
                        "    \"eventTime\": \"12:00:00\",\n" +
                        "    \"location\": \"testLocation\",\n" +
                        "    \"postalCode\": \"testPostalCode\",\n" +
                        "    \"countryCode\": \"testCountry\",\n" +
                        "    \"description\": \"testDescription\",\n" +
                        "    \"category\": \"testCategory\",\n" +
                        "    \"image\": \"testImage\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        // Create a testEvent object
        Event testEvent = getEvent();

        // Stub the getEvent and saveEvent methods of eventService
        Mockito.when(eventService.getEvent(1L)).thenReturn(testEvent);

        Event updatedTestEvent = getEvent();
        updatedTestEvent.setEventName("testEventUpdated");
        Mockito.when(eventService.saveEvent(Mockito.any(Event.class))).thenReturn(updatedTestEvent);

        // Perform the request
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        // Get the response
        MockHttpServletResponse response = result.getResponse();

        // Assert the response status and content
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("testEventUpdated"));
    }

    @Test
    public void testUpdateEventNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/events/update/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"eventName\": \"testEvent\",\n" +
                        "    \"email\": \"testEmail\",\n" +
                        "    \"eventDate\": \"2021-09-27\",\n" +
                        "    \"eventTime\": \"12:00:00\",\n" +
                        "    \"location\": \"testLocation\",\n" +
                        "    \"postalCode\": \"testPostalCode\",\n" +
                        "    \"countryCode\": \"testCountry\",\n" +
                        "    \"description\": \"testDescription\",\n" +
                        "    \"category\": \"testCategory\",\n" +
                        "    \"image\": \"testImage\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(eventService.getEvent(1L)).thenReturn(null);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateEventBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/events/update/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"eventName\": \"testEvent\",\n" +
                        "    \"email\": \"testEmail\",\n" +
                        "    \"eventDate\": \"2021-09-27\",\n" +
                        "    \"eventTime\": \"12:00:00\",\n" +
                        "    \"location\": \"testLocation\",\n" +
                        "    \"postalCode\": \"testPostalCode\",\n" +
                        "    \"countryCode\": \"testCountry\",\n" +
                        "    \"description\": \"testDescription\",\n" +
                        "    \"category\": \"testCategory\",\n" +
                        "    \"image\": \"testImage\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        Event testEvent = getEvent();

        Mockito.when(eventService.getEvent(1L)).thenReturn(testEvent);

        Mockito.when(eventService.saveEvent(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }

    @Test
    public void testDeleteEvent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/events/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Event testEvent = getEvent();

        Mockito.when(eventService.getEvent(1L)).thenReturn(testEvent);
        Mockito.when(eventService.deleteEvent(1L)).thenReturn(true);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus()); // Changed from HttpStatus.OK to HttpStatus.NO_CONTENT
        assertEquals("", response.getContentAsString());
    }

    @Test
    public void testDeleteEventNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/events/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(eventService.getEvent(1L)).thenReturn(null);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("", response.getContentAsString());
    }

    @Test
    public void testDeleteEventBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/events/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Event testEvent = getEvent();

        Mockito.when(eventService.getEvent(1L)).thenReturn(testEvent);
        Mockito.when(eventService.deleteEvent(1L)).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Exception"));
    }
}
