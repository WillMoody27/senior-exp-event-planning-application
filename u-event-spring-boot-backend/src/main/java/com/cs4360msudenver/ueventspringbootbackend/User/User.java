package com.cs4360msudenver.ueventspringbootbackend.User;

import javax.persistence.*;

import com.cs4360msudenver.ueventspringbootbackend.Event.Event;
import com.cs4360msudenver.ueventspringbootbackend.Interests.Interests;
import com.cs4360msudenver.ueventspringbootbackend.image.Image;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    private static String jwt;


    public User(String username, String password, ProviderEnum provider) {
        this.username = username;
        this.password = password;
        this.provider = provider;
    }

    public User(String jwt) {
        this.jwt = jwt;
    }

    public enum userRole {
        ADMIN,
        USER
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

//    @Transient
//    @Column(name = "file")
//    private MultipartFile file;

    // JWT token
    private String token;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "job_description")
    private String jobDescription;

    @Column(name = "address")
    private String address;

    @Column(name = "postal_code")
    private String postalCode;

    //For Designated Roles
    @Enumerated(EnumType.STRING)
    private userRole role;

    //    For OAuth2 (Google and Facebook, etc.)
    @Enumerated(EnumType.STRING)
    private ProviderEnum provider;

    // Create a list of events that the user is attending
    @ManyToMany
    @Column(name = "attending_events")
    private Set<Event> events;

    // Create a list of events that the user is attending
    @ManyToMany
    @Column(name = "user_interests")
    private Set<Interests> interests;




    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
//

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getPassword() {

        return this.password;
    }


    public ProviderEnum getProvider() {
        return provider;
    }

    public void setProvider(ProviderEnum provider) {
        this.provider = provider;
    }

    public userRole getRole() {
        return role;
    }

    public void setRole(userRole role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
        // This was updated to add the role "ROLE_USER" to the list of authorities
        return Collections.singleton(
                new SimpleGrantedAuthority("USER")
        );
    }

    public String getUsername() {
        return username;
    }

    public static String getJwt() {
        return jwt;
    }

    public static void setJwt(String jwt) {
        User.jwt = jwt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
//

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
//        return false;
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return false;
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return false;
        return true;
    }

//
//
////    This is for OAuth2 (Google and Facebook, etc.)
//    public ProviderEnum getProvider() {
//        return provider;
//    }
//
//    public void setProvider(ProviderEnum provider) {
//        this.provider = provider;
//    }

}



