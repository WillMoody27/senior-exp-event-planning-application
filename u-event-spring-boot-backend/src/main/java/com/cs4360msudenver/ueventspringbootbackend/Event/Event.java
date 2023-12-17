package com.cs4360msudenver.ueventspringbootbackend.Event;

import javax.persistence.*;

import com.cs4360msudenver.ueventspringbootbackend.User.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//import java.sql.Time;
import java.time.LocalTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "event")
@NoArgsConstructor
@AllArgsConstructor
//@IdClass(EventId.class)
public class Event {

    @Id
    @Column(name = "event_id", columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long eventId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "username")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String username;

    @ManyToOne()
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User user;

    @Column(name = "event_date")
    private Date eventDate;

    @Column(name = "event_time")
    //private Time eventTime;
    private LocalTime eventTime;

    @Column(name = "event_location")
    private String location;

    @Column(name = "postal_code")
    private String postalCode;
    //    City
    @Column(name = "city")
    private String city;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "event_description")
    private String description;

    @Column(name = "event_category")
    private String category;

    @Column(name = "event_image")
    private String image;

    //    Image file
    @Column(name = "event_image_file")
    private String imageFile;

    //    Tags Added
    @ElementCollection
    private List<String> tags;

    @ElementCollection
    private Set<String> attendees;
}