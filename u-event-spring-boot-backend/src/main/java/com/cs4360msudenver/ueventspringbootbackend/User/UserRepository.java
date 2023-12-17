package com.cs4360msudenver.ueventspringbootbackend.User;

import com.cs4360msudenver.ueventspringbootbackend.Interests.Interests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

//    Get the interestId of a user
    boolean existsByUsername(String username);
}
