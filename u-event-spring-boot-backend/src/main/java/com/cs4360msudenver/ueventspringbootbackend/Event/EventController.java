package com.cs4360msudenver.ueventspringbootbackend.Event;

import com.cs4360msudenver.ueventspringbootbackend.User.User;
import com.cs4360msudenver.ueventspringbootbackend.User.UserService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    //    This is a GET request to get all the user from the database using PSQL
    @GetMapping
    public ResponseEntity<List<Event>> getEvents() {
        try {
            // return the list of users using the HTTP status code 200 OK
            return new ResponseEntity<>(eventService.getEvents(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        try {
            Event event = eventService.getEvent(id);
            return new ResponseEntity<>(event, event == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        try {
            return new ResponseEntity<>(eventService.saveEvent(event), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
        try {
            Event retrievedEvent = eventService.getEvent(id);
            if (retrievedEvent == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            retrievedEvent.setEventName(updatedEvent.getEventName());
            retrievedEvent.setEventDate(updatedEvent.getEventDate());
            retrievedEvent.setLocation(updatedEvent.getLocation());
            retrievedEvent.setEventTime(updatedEvent.getEventTime());
            retrievedEvent.setCity(updatedEvent.getCity());
            retrievedEvent.setDescription(updatedEvent.getDescription());
            retrievedEvent.setCategory(updatedEvent.getCategory());
            retrievedEvent.setPostalCode(updatedEvent.getPostalCode());
            retrievedEvent.setCountryCode(updatedEvent.getCountryCode());
            retrievedEvent.setImage(updatedEvent.getImage());
            retrievedEvent.setTags(updatedEvent.getTags());
            retrievedEvent.setImageFile(updatedEvent.getImageFile());
            return new ResponseEntity<>(eventService.saveEvent(retrievedEvent), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable Long id) {
        try {
            if (eventService.deleteEvent(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

//    // Request for managing users attending the event
}