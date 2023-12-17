import React, { useState, useEffect } from "react";
import "../../css/EventCard.css";
import { AvatarImage } from "../commonElements";
import {
  fetchAndSetImageBase64,
  fetchImageByUsername,
} from "../../handlers/api";

const EventCard = ({
  width,
  height,
  attendees,
  fontSize,
  eventName,
  eventDate,
  eventTime,
  location,
  city,
  postalCode,
  description,
  category,
  image,
  user: { username, firstName, lastName },
  tags,
  backgroundImage,
}) => {
  const [imageBase64, setImageBase64] = useState(null);
  const [imageId, setImageId] = useState([]);
  const userAttending = (attendees || []).includes(
    localStorage.getItem("username") || ""
  );

  const eventDateFormatted = eventDate
    ? new Date(eventDate).toLocaleDateString("en-US", {
        year: "numeric",
        month: "short",
        day: "numeric",
      })
    : null;

  // // HandlerMethods
  // const handleSettingImageId = async () => {
  //   const imageId = await fetchImageId(username);
  //   setImageId(imageId);
  // };

  useEffect(() => {
    const fetchAndSetImage = async () => {
      try {
        const data = await fetchImageByUsername(username);
        setImageId(data);

        if (data) {
          const base64String = await fetchAndSetImageBase64(data);
          setImageBase64(base64String);
        }
      } catch (error) {
        console.error("Error fetching image:", error);
      }
    };

    fetchAndSetImage();
  }, [username]);

  // // USEFFECT
  // useEffect(() => {
  //   handleSettingImageId();
  // }, [username]);

  return (
    <article
      className="event-card"
      style={{
        width: width + "px",
        height: height + "px",
        fontSize: fontSize + "px",
        backgroundImage: `linear-gradient(to top, rgba(0,0,0,1), rgba(0,0,0,.1)), url(${
          backgroundImage ? backgroundImage : image
        })`,
        backgroundRepeat: "no-repeat",
        backgroundPosition: "center center",
        backgroundSize: "cover",
      }}
    >
      {imageBase64 ? (
        <img
          className="event-avatar"
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
          className="event-avatar"
          src={AvatarImage}
          width={100}
          height={100}
          alt="image_alt"
          style={{
            objectFit: "cover",
          }}
        />
      )}
      <p className="match-tag">Match</p>
      <div
        className="card-content"
        style={{
          fontSize: fontSize - 25 + "px",
          animation: `contentRightInAnim 0.9s ease-in-out`,
        }}
      >
        <h2 className="card-title">{eventName}</h2>
        <p className="attending-tag-event-card match-tag">
          Attending ({attendees ? attendees.length : ""})
        </p>
        <p>Host: {`${firstName} ${lastName}`}</p>
        <p
          className="category"
          style={{
            textTransform: "capitalize",
          }}
        >
          Category: {category}
        </p>
        <p>
          Date: {eventDateFormatted}
          {eventTime && ` | ${eventTime}`}
        </p>
        <p>
          Location: {`${location ? location + ", " : ""}${city} ${postalCode}`}
        </p>
        <p className="event-details">
          <span className="event-details-header">Event Details:</span> <br />{" "}
          <span className="event-details-text">{description}</span>
        </p>
        <div className="event-tags">
          {tags.map((tag, index) => (
            <p className="event-tag" key={index}>{`#${tag}`}</p>
          ))}
        </div>
        <div className="attend-block">
          {
            // Determine if the username is in the attendees array
            userAttending ? (
              <button
                className="attend-btn"
                style={{
                  backgroundColor: "red",
                }}
              >
                Attending
              </button>
            ) : (
              <button className="attend-btn">Attend</button>
            )
          }
          <div className="contact-details">
            <p>User: {`${firstName} ${lastName}`}</p>
            <p>Member Since: {2023}</p>
          </div>
        </div>
      </div>
    </article>
  );
};

export default EventCard;
