import React, { useEffect, useState } from "react";
import { Link, Params, redirect } from "react-router-dom";
import "../../css/EventDetails.css";
import { AvatarImage } from "../commonElements";
import { fetchUserDetails, fetchImageData } from "../../handlers/api"; // Import the new API functions

// Use the useLocation hook to get the event object passed from the EventCard component
import { useLocation } from "react-router-dom";

// TODO-Complete: Separate EventPrompt component
const EventPrompt = ({ eventName, userName }) => {
  return (
    <div>
      <p className="event-prompt-header-1">
        Current Viewing: <span className="event-name-bold">{eventName}</span> by
        Event <span className="event-name-bold">{userName}</span>
      </p>
    </div>
  );
};

// TODO-Complete: Reusable Date Component
const DateComponent = ({ eventDate }) => {
  const date = `${new Date(eventDate).toLocaleDateString("en-US", {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  })}\n${new Date(eventDate).toLocaleTimeString("en-US", {
    hour: "numeric",
    minute: "numeric",
    hour12: true,
  })}`;
  return <p>Meeting Time:{date}</p>;
};

// TODO-Complete: Tag Component
const TagComponent = ({ tags }) => {
  // Fragment used to avoid unnecessary divs in the DOM
  return (
    <>
      {tags.map((tag, index) => {
        return (
          <p className="event-tag-content" key={index}>
            {`#${tag}`}
          </p>
        );
      })}
    </>
  );
};

// TODO-Complete: Main EventDetails component + subcomponents
const EventDetails = () => {
  const [imageBase64, setImageBase64] = useState(null);
  const [joinStatus, setJoinStatus] = useState(false);
  const loc = useLocation();
  const { event } = loc.state || {};
  const loggedInUser = localStorage.getItem("username");
  const {
    eventId,
    attendees,
    eventName,
    user: { username, firstName, lastName },
    eventDate,
    location,
    description,
    image,
    tags,
  } = event;

  const userName_s = `${firstName} ${lastName}`;

  useEffect(() => {
    fetchUserDetails(username)
      .then((userData) => {
        const imageId = userData[0]; // Assuming the first item is the image ID
        fetchImageData(imageId)
          .then((data) => {
            setImageBase64(data.image);
          })
          .catch(() => setImageBase64(null));
      })
      .catch(() => setImageBase64(null));
  }, [username]);

  useEffect(() => {
    if (attendees.includes(loggedInUser)) {
      setJoinStatus(true);
    }
  }, [attendees, loggedInUser]);

  // Allows users to join the event
  const handleJoinEvent = (e) => {
    const loggedInUser = localStorage.getItem("username");
    e.preventDefault();
    // http://localhost:8080/api/users/50cent@email.com/events/13

    if (joinStatus) {
      // Unjoin Event (DELETE REQUEST)
      // http://localhost:8080/api/users/50cent@email.com/events/13/unjoin
      fetch(
        `http://localhost:8080/api/users/${loggedInUser}/events/${eventId}/unjoin`,
        {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            eventId,
            attendees: [loggedInUser],
          }),
        }
      )
        .then((data) => {
          setJoinStatus(false);
          // console.log("Unjoin Event: ", data);
          // console.log("Join Status: ", joinStatus);
        })
        .catch((error) => {
          console.log("Error: ", error);
        });
    } else {
      // Join Event
      fetch(
        `http://localhost:8080/api/users/${loggedInUser}/events/${eventId}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            eventId,
            attendees: [loggedInUser],
          }),
        }
      )
        .then((data) => {
          setJoinStatus(true);
          // console.log("Join Event: ", data);
          // console.log("Join Status: ", joinStatus);
        })
        .catch((error) => {
          console.log("Error: ", error);
        });
    }
  };

  return (
    <section className="event-detail"
    data-testid = "event-detail">
      <div className="event-card-container">
        {/* EVENT CARD */}
        <article
          className="event-card-content"
          style={{
            backgroundImage: `linear-gradient(
              rgba(0,0,0,1), 
              rgba(0,0,0,.6), 
              rgba(0,0,0,1)), 
              url(${image})`,
          }}
        >
          <Link to="/" className="btn back-btn">
            {"\u2190 Event Board"}
          </Link>
          <div className="tag-section">
            <p className="event-details-match-tag">Tags</p>
            <div className="event-tag-container">
              {/* Tag Component */}
              <TagComponent tags={tags} />
              {/* End of Tag Component */}
            </div>
          </div>
          {
            // Check the image
            imageBase64 ? (
              <img
                className="event-details-avatar"
                src={`data:image/jpeg;base64,${imageBase64}`}
                width={100}
                height={100}
                alt="image_alt"
                style={{
                  objectFit: "cover",
                }}
              />
            ) : (
              <img
                className="event-details-avatar"
                src={AvatarImage}
                width={100}
                height={100}
                alt="image_alt"
              />
            )
          }
          <p className="attending-tag match-tag">
            Attending ({attendees.length})
          </p>
          <div className="card-content">
            <h2 className="card-title">{eventName}</h2>
            <p>Host: {userName_s}</p>
            {/* Date Component */}
            <DateComponent eventDate={eventDate} />
            {/* End of Date Component */}
            <p>Location: {location}</p>
            <div className="contact-block">
              <p className="event-details">
                <span className="event-details-header">Event Details:</span>{" "}
                <br /> <span className="event-details-text">{description}</span>
              </p>
              <button className="contact-btn">Match</button>
              <div className="contact-details">
                <p>User: {userName_s}</p>
                <p>Memeber Since: {2023}</p>
              </div>
            </div>
            {/* Use the reusable Button component */}
            <div className="btn-container">
              <button
                className={
                  joinStatus
                    ? "join-status-btn btn joined"
                    : "join-status-btn btn"
                }
                onClick={(e) => {
                  // handleEventJoinStatus();
                  handleJoinEvent(e);
                }}
              >
                {joinStatus ? "Joined" : "Join"}
              </button>
            </div>
          </div>
        </article>
        {/* End of EVENT CARD */}
      </div>
      {/* EventPrompt component */}
      <EventPrompt eventName={eventName} userName={userName_s} />
      {/* End of EventPrompt component */}
    </section>
  );
};

export default EventDetails;
