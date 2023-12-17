package com.cs4360msudenver.ueventspringbootbackend.Interests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class InterestService {

    @Autowired
    private InterestRepository interestRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    //Get all interests
    public List<Interests> getInterests() {
        return interestRepository.findAll();
    }


    // Get interest by id
    public Interests getInterest(Long id) {
        try {
            return interestRepository.findById(id).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get interest by name

    public Interests getInterestByName(String interest) {
        try {
            return interestRepository.findByInterest(interest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Save interest
    @Transactional
    public Interests saveInterest(Interests interest) {
        interest = interestRepository.saveAndFlush(interest);
        entityManager.refresh(interest);
        return interest;
    }

    // delete interest
    public boolean deleteInterest(Long id) {

        try {
            if (interestRepository.existsById((id))) {
                interestRepository.deleteById(id);
                return true;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }


    // Get interest by username service method
    public List<Interests> findInterestsByUsers(String username) {
        try {
            return interestRepository.findInterestsByUsers(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
