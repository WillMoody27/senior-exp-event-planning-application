package com.cs4360msudenver.ueventspringbootbackend.User;

import com.cs4360msudenver.ueventspringbootbackend.Event.Event;
import com.cs4360msudenver.ueventspringbootbackend.Event.EventService;
import com.cs4360msudenver.ueventspringbootbackend.Interests.InterestService;
import com.cs4360msudenver.ueventspringbootbackend.Interests.Interests;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private InterestService interestService;

    // Add custom user details service for authentication and authorization
    @Autowired
    private UserDetailsService userDetailsService;
    // Add authentication manager for authentication and authorization
    @Autowired
    private AuthenticationManager authenticationManager;

    // Add JWT utility for authentication and authorization
    @Autowired
    private JwtUtil jwtUtil;


//    PASSWORD ENCODING

    @GetMapping
    public ResponseEntity<Iterable<User>> getUsers() {
        try {
            return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping(path = "/{id}", produces = "application/json")
//    public ResponseEntity<User> getUser(@PathVariable Long id) {
//        try {
//            User user = userService.getUser(id);
//            return new ResponseEntity<>(user, user == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
//    }

//    // Working Code.....
//    @PostMapping("/login")
//    public User registerUser(@RequestBody User user) {
//        return userService.createUser(user);
//    }


//    @PostMapping("/login")
//    public String login(@RequestBody User user) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
//        );
//
////        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
//        return jwtUtil.generateToken(user);
//    }

//    @PostMapping("/login")
//    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
//            );
//        } catch (BadCredentialsException e) {
//            throw new Exception("Incorrect username or password", e);
//        }
//
//        String username = authenticationRequest.getUsername();
//        String password = authenticationRequest.getPassword();
//
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        final String jwt = jwtUtil.generateToken(String.valueOf(userDetails));
//
//        return ResponseEntity.ok(new AuthenticationResponse(jwt));
//    }
//

    @PostMapping("/login")
    public ResponseEntity<String> generateToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(jwt);
    }

    //Get user by username
    @GetMapping(path = "/{username}", produces = "application/json")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        UserDetails checkUser = userDetailsService.loadUserByUsername(username);

        try {
            User user = userService.getUserByEmail(checkUser.getUsername());
            return new ResponseEntity<>(user, user == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping(path ="/login")
//    public ResponseEntity<User> signInUser(@RequestBody User user) {
//        //use loadUserByUsername to check if user exists
//        UserDetails checkUser = userService.loadUserByUsername(user.getEmail());
//        User retrievedUser = userService.getUserByEmail(checkUser.getUsername());
//
//
//    }


    @PutMapping("/{username}")
    public ResponseEntity<Object> updateUser(@PathVariable("username") String username, @RequestBody User updatedUser) {
        UserDetails checkUser = userDetailsService.loadUserByUsername(username);
        User retrievedUser = userService.getUserByEmail(checkUser.getUsername());
        try {
            if (retrievedUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            retrievedUser.setFirstName(updatedUser.getFirstName());
            retrievedUser.setLastName(updatedUser.getLastName());
            retrievedUser.setPassword(updatedUser.getPassword());
            retrievedUser.setJobDescription(updatedUser.getJobDescription());
            retrievedUser.setPhoneNumber(updatedUser.getPhoneNumber());
            retrievedUser.setNickname(updatedUser.getNickname());
            retrievedUser.setAddress(updatedUser.getAddress());
            retrievedUser.setPostalCode(updatedUser.getPostalCode());

            return ResponseEntity.ok(userService.saveUser(retrievedUser));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("username") String username) {
        UserDetails checkUser = userDetailsService.loadUserByUsername(username);
        User user = userService.getUserByEmail(checkUser.getUsername());

        try {
            if (userService.deleteUser(user.getUserId())) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // This request allows the user to register for an event in the db.
    @PostMapping("/{username}/events/{eventId}")
    public ResponseEntity<String> signupForEvent(@PathVariable String username, @PathVariable Long eventId) {
        UserDetails checkUser = userDetailsService.loadUserByUsername(username);
        User user = userService.getUserByEmail(checkUser.getUsername());
        Event event = eventService.getEvent(eventId);

        try {
            // append the event id to the user's attending events list
            user.getEvents().add(event);
            userService.saveUser(user);

            // append the username to the event's attendees list
            event.getAttendees().add(username);
            eventService.saveEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("User " + user.getUsername() + " signed up for event " + event.getEventName(), HttpStatus.OK);
    }

    //    This is method to delete the user from the attendees list
    @DeleteMapping("/{username}/events/{eventId}/unjoin")
    public ResponseEntity<String> unJoinForEvent(@PathVariable String username, @PathVariable Long eventId) {
        UserDetails checkUser = userDetailsService.loadUserByUsername(username);
        User user = userService.getUserByEmail(checkUser.getUsername());
        Event event = eventService.getEvent(eventId);

        try {
            // remove the event id to the user's attending events list
            user.getEvents().remove(event);
            userService.saveUser(user);

            // append the username to the event's attendees list
            event.getAttendees().remove(username);
            eventService.saveEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("User " + user.getUsername() + " left the event " + event.getEventName(), HttpStatus.OK);
    }

    // Request for managing user's events
    @PostMapping("/{username}/interests/{interest}")
    public ResponseEntity<String> selectedInterests(@PathVariable String username, @PathVariable String interest) {
        UserDetails checkUser = userDetailsService.loadUserByUsername(username);
        User user = userService.getUserByEmail(checkUser.getUsername());
        Interests interests1 = interestService.getInterestByName(interest);

        try {
            // append the event id to the user's attending events list
            user.getInterests().add(interests1);
            userService.saveUser(user);

            // append the username to the event's attendees list
            interests1.getUsers().add(username);
            interestService.saveInterest(interests1);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("User " + user.getUsername() + " has selected interest: " + interests1.getInterest(), HttpStatus.OK);
    }

    // Delete the interest Id from the user's interest list
    @DeleteMapping("/{username}/interests/{id}")
    public ResponseEntity<String> deleteUserInterest(@PathVariable String username, @PathVariable Long id) {
        UserDetails checkUser = userDetailsService.loadUserByUsername(username);
        User user = userService.getUserByEmail(checkUser.getUsername());
        Interests selectedUserInterest = interestService.getInterest(id);

        try {
            // remove the event id to the user's attending events list
            user.getInterests().remove(selectedUserInterest);
            userService.saveUser(user);

            // append the username to the event's attendees list
            selectedUserInterest.getUsers().remove(username);
            interestService.saveInterest(selectedUserInterest);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("User " + user.getUsername() + " has removed interest: " + selectedUserInterest.getInterest(), HttpStatus.OK);
    }
}