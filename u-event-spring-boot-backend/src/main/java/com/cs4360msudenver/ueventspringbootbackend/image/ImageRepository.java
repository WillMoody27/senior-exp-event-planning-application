package com.cs4360msudenver.ueventspringbootbackend.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    // Custom repository method to find the image ID by username
//    @Query("SELECT i.id FROM Image i WHERE i.username = ?1")
//    Optional<Long> findIdByUsername(String username);

    @Query("SELECT i.id FROM Image i WHERE i.username = ?1")
    List<Long> findIdsByUsername(String username);


    // Custom repository method to find an image by its username
    Optional<Image> findByUsername(String username);

}