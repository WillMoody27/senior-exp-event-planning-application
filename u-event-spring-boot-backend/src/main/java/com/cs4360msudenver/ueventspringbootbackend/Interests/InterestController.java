package com.cs4360msudenver.ueventspringbootbackend.Interests;

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
@RequestMapping("/api/interests")
public class InterestController {

    private InterestService interestService;
    private UserDetailsService userDetailsService;
    private UserService userService;

    @Autowired
    public InterestController(InterestService interestService, UserDetailsService userDetailsService, UserService userService) {
        this.interestService = interestService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }


    @GetMapping

    public ResponseEntity<Iterable<Interests>> getInterests() {
        try {
            return new ResponseEntity<>(interestService.getInterests(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get interest by id
    @GetMapping(path = "/id/{id}")
    public ResponseEntity<Interests> getInterest(@PathVariable Long id) {
        try {
            Interests interest = interestService.getInterest(id);
            return new ResponseEntity<>(interest, interest == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    // Get interest by name
    @GetMapping(path = "/{interest}")
    public ResponseEntity<Interests> getInterestByName(@PathVariable String interest) {
        try {
            Interests interests = interestService.getInterestByName(interest);
            return new ResponseEntity<>(interests, interests == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    // Save interest
    @PostMapping
    public ResponseEntity<Interests> saveInterest(@RequestBody Interests interest) {
        try {
            return new ResponseEntity<>(interestService.saveInterest(interest), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    // delete interest
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteInterest(@PathVariable Long id) {
        try {
            if (interestService.deleteInterest(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get interests by username service method
    @GetMapping(path = "/username/{username}")
    public ResponseEntity<Interests> getInterestByUsername(@PathVariable String username) {
        UserDetails checkUser = userDetailsService.loadUserByUsername(username);
        User user = userService.getUserByEmail(checkUser.getUsername());
        try {
            List<Interests> interests = interestService.findInterestsByUsers(user.getUsername());
            return new ResponseEntity(interests, interests == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }
}
