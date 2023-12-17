package com.cs4360msudenver.ueventspringbootbackend.User;

import com.cs4360msudenver.ueventspringbootbackend.Event.Event;
import com.cs4360msudenver.ueventspringbootbackend.Event.EventRepository;
import com.cs4360msudenver.ueventspringbootbackend.Interests.Interests;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        try {
            return userRepository.findById(id).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUserByEmail(String username) {
        try {
            return userRepository.findByUsername(username).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public User saveUser(User user) {
        user = userRepository.saveAndFlush(user);
        entityManager.refresh(user);
        return user;
    }

    public boolean deleteUser(Long id) {
        try {
            if (userRepository.existsById((id))) {
                userRepository.deleteById(id);
                return true;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return false;
    }

    //    PASSWORD ENCODING
    public User createUser(User user) {

        UserDetails userDetails = user;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setToken(jwtUtil.generateToken(userDetails));

        return userRepository.save(user);
    }

    // Service method to get the list of interests of a user
    // -------------------ADD Attendees---------------------------------------------

}
