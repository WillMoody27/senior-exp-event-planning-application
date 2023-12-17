package com.cs4360msudenver.ueventspringbootbackend.Interests;


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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = InterestService.class)
public class InterestServiceTest {

    @MockBean
    private InterestRepository interestRepository;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EntityManager entityManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private InterestService interestService;

    @BeforeEach
    public void setup() {
        interestService.entityManager = entityManager;
    }

    @Test
    public void testGetInterests() throws Exception {
        Interests testInterest = new Interests();
        testInterest.setId(1L);
        testInterest.setInterest("testInterest1");


        Interests testInterest2 = new Interests();
        testInterest2.setId(2L);
        testInterest2.setInterest("testInterest2");

        Mockito.when(interestService.getInterests()).thenReturn(Arrays.asList(testInterest, testInterest2));

        List<Interests> interests = interestService.getInterests();
        assertEquals(2L, interests.size()); //checks for two interest
        assertEquals(1L, interests.get(0).getId()); //checks whether the first interest is the right Id
        assertEquals(2L, interests.get(1).getId()); //check if second interest is the right Id


    }

    @Test
    public void testGetInterest() throws Exception {
        Interests testInterest = new Interests();
        testInterest.setId(1L);
        testInterest.setInterest("testInterest1");

        Mockito.when(interestRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testInterest));
        Interests interest = interestService.getInterest(1L);
        assertEquals(1L, interest.getId());
        assertEquals("testInterest1", interest.getInterest());
    }

    @Test
    public void testGetInterestNotFound() throws Exception{
        Mockito.when(interestRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Interests interest = interestService.getInterest(1L);
        assertEquals(null, interest);
    }

    @Test
    public void testGetInterestByName() throws Exception {
        Interests testInterest = new Interests();
        testInterest.setId(1L);
        testInterest.setInterest("testInterest1");

        Mockito.when(interestRepository.findByInterest(Mockito.any())).thenReturn(testInterest);
        Interests interest = interestService.getInterestByName("testInterest1");
        assertEquals(1L, interest.getId());
        assertEquals("testInterest1", interest.getInterest());
    }

    @Test
    public void testGetInterestByNameNotFound() throws Exception{
        Mockito.when(interestRepository.findByInterest(Mockito.any())).thenReturn(null);
        Interests interest = interestService.getInterestByName("testInterest1");
        assertEquals(null, interest);
    }

    @Test
    public void testGetInterestByNameException() throws Exception {
        // Mock the interestRepository to throw an exception
        Mockito.when(interestRepository.findByInterest(Mockito.any())).thenThrow(IllegalArgumentException.class);

        Interests interest = interestService.getInterestByName("testInterest1");

        // Your catch block should handle the exception
        assertNull(interest);
    }





//    @Test
//    public void testGetInterestByNameNotFound() throws Exception {
//        Interests testInterest = new Interests();
//        testInterest.setId(1L);
//        testInterest.setInterest("testInterest1");
//
//        Mockito.when(interestRepository.findByInterest(Mockito.any())).thenReturn(null);
//        Interests interest = interestService.getInterestByName("testInterest1");
//        assertEquals(null, interest);
//    }

    @Test
    public void testSaveInterest() throws Exception {
        Interests interest = new Interests();
        interest.setInterest("testInterest1");
        interest.setId(1L);

        Mockito.when(interestRepository.saveAndFlush(Mockito.any())).thenReturn(interest);
        Mockito.when(interestRepository.save(Mockito.any())).thenReturn(interest);

        assertEquals(interest, interestService.saveInterest(interest));
    }

    @Test
    public void testSaveInterestBadRequest() throws Exception {
        Interests badInterest = new Interests();
        badInterest.setInterest("Bad Interest");

        Mockito.when(interestRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        Mockito.when(interestRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            interestService.saveInterest(badInterest);
        });
    }

    @Test
    public void testDeleteInterest() throws Exception {
        Interests interest = new Interests();
        interest.setInterest("testInterest1");
        interest.setId(1L);
        Mockito.when(interestRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(interest));
        Mockito.when(interestRepository.existsById(Mockito.any())).thenReturn(true);

        assertTrue(interestService.deleteInterest(interest.getId()));
    }

    @Test
    public void testDeleteInterestNotFound() throws Exception {
        Mockito.when(interestRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(interestRepository.existsById(Mockito.any())).thenReturn(false);

        assertFalse(interestService.deleteInterest(-1L)); // Cannot have a tile with a negative id
    }

    @Test
    public void testDeleteInterestException() throws Exception {
        Interests interest = new Interests();
        interest.setInterest("testInterest1");
        interest.setId(1L);

        // Mock the interestRepository to throw an exception
        Mockito.when(interestRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(interest));
        Mockito.when(interestRepository.existsById(Mockito.any())).thenReturn(true);
        Mockito.doThrow(IllegalArgumentException.class).when(interestRepository).deleteById(Mockito.anyLong());

        assertFalse(interestService.deleteInterest(interest.getId())); // Expecting it to return false
    }

    @Test
    public void testFindInterestsByUsers() throws Exception {
        // Arrange
        String testUsername = "testUser";
        Interests testInterest1 = new Interests(1L, "Interest1", new HashSet<>(List.of(testUsername)));
        Interests testInterest2 = new Interests(2L, "Interest2", new HashSet<>(List.of(testUsername)));

        // Assuming the method findInterestsByUsers takes a username and returns a list of Interests
        Mockito.when(interestRepository.findInterestsByUsers(testUsername)).thenReturn(Arrays.asList(testInterest1, testInterest2));

        // Act
        List<Interests> interests = interestService.findInterestsByUsers(testUsername);

        // Assert
        assertNotNull(interests);
        assertEquals(2, interests.size());
        assertTrue(interests.contains(testInterest1));
        assertTrue(interests.contains(testInterest2));
    }

}


