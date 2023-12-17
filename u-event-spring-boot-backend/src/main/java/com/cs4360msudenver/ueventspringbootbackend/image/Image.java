package com.cs4360msudenver.ueventspringbootbackend.image;

import com.cs4360msudenver.ueventspringbootbackend.User.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Data
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Have to use @Lob annotation to store the image as a blob in the database
    @Lob
    @Column(name = "image", length = Integer.MAX_VALUE, nullable = true)
    private String image;

    @Column(name = "username")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String username;

    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User user;
}