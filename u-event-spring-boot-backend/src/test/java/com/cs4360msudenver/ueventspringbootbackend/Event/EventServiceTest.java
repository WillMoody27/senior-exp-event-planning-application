package com.cs4360msudenver.ueventspringbootbackend.Event;

import com.cs4360msudenver.ueventspringbootbackend.User.CustomUserDetailsService;
import com.cs4360msudenver.ueventspringbootbackend.User.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = EventService.class)
public class EventServiceTest {

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EntityManager entityManager;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtUtil jwtUtil;


    @Autowired
    private EventService eventService;

    @BeforeEach
    public void setup() {
        eventService.entityManager = entityManager;
    }

    @Test
    public void testGetEvents() {
        Event testEvent = new Event();
        testEvent.setEventName("testEvent");

        Mockito.when(eventRepository.findAll()).thenReturn(List.of(testEvent));

        List<Event> events = eventService.getEvents();
        assertEquals(1, events.size());
        assertEquals("testEvent", events.get(0).getEventName());
    }

    @Test
    public void testGetEvent() {
        Event testEvent = new Event();
        testEvent.setEventName("testEvent");

        Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));

        Event event = eventService.getEvent(1L);
        assertEquals("testEvent", event.getEventName());
    }

    @Test
    public void testGetEventNotFound() {
        Mockito.when(eventRepository.findById(2L)).thenReturn(Optional.empty());

        Event event = eventService.getEvent(2L);
        assertNull(event);
    }

    @Test
    public void testSaveEvent() {
        Event testEvent = new Event();
        testEvent.setEventName("testEvent");

        Mockito.when(eventRepository.saveAndFlush(testEvent)).thenReturn(testEvent);

        Event event = eventService.saveEvent(testEvent);
        assertEquals("testEvent", event.getEventName());
    }

    @Test
    public void testSaveEventError() {
        Event testEvent = new Event();
        testEvent.setEventName("testEvent");

        // Simulating an error by returning null.
        Mockito.when(eventRepository.saveAndFlush(testEvent)).thenReturn(null);

        Event event = eventService.saveEvent(testEvent);
        assertNull(event);
    }

    @Test
    public void testDeleteEvent() {
        Event testEvent = new Event();
        testEvent.setEventName("testEvent");

        Mockito.when(eventRepository.existsById(1L)).thenReturn(true);

        boolean eventDeleted = eventService.deleteEvent(1L);
        assertTrue(eventDeleted);
    }

    @Test
    public void testDeleteEventNotFound() {
        Mockito.when(eventRepository.existsById(1L)).thenReturn(false);

        boolean eventDeleted = eventService.deleteEvent(1L);
        assertFalse(eventDeleted);
    }

    @Test
    public void testDeleteEventError() {
        Mockito.when(eventRepository.existsById(1L)).thenThrow(IllegalArgumentException.class);

        boolean eventDeleted = eventService.deleteEvent(1L);
        assertFalse(eventDeleted);
    }
}