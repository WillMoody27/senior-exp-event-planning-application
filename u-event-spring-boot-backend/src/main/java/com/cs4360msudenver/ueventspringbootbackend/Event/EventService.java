package com.cs4360msudenver.ueventspringbootbackend.Event;


import com.cs4360msudenver.ueventspringbootbackend.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Event getEvent(Long id) {
        try {
            return eventRepository.findById(id).get();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Event saveEvent(Event event) {
        event = eventRepository.saveAndFlush(event);
        entityManager.refresh(event);
        return event;
    }

    public boolean deleteEvent(Long id) {
        try {
            if(eventRepository.existsById((id))){
                eventRepository.deleteById(id);
                return true;
            }
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }

        return false;
    }

    // -------------------ADD Attendees------------------------------

}
