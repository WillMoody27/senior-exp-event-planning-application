import React, { useState, useEffect } from "react";
import { AvatarImage } from "../commonElements";
import {
  fetchImageId,
  fetchAndSetImageBase64,
  fetchImageByUsername,
} from "../../handlers/api";

const EventCardForm = ({
  isEdit,
  onSubmit,
  onChange,
  editing,
  width,
  height,
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
  user: { firstName, lastName },
  tags,
  backgroundImage,
  attendees,
  username,
}) => {
  const [formData, setFormData] = useState({
    width,
    height,
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
    firstName,
    lastName,
    tags,
    backgroundImage,
    attendees,
    username,
  });

  useEffect(() => {
    if (editing) {
      setFormData({
        width,
        height,
        fontSize,
        eventName,
        eventDate,
        eventTime: eventTime ? eventTime.substring(0, 5) : "",
        location,
        city,
        postalCode,
        description,
        category,
        image,
        firstName,
        lastName,
        tags,
        backgroundImage,
        attendees,
        username,
      });
    }
  }, [
    editing,
    width,
    height,
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
    firstName,
    lastName,
    tags,
    backgroundImage,
    attendees,
    username,
  ]);

  const [imageBase64, setImageBase64] = useState(null);
  const [imageId, setImageId] = useState([]);

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

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });

    if (onChange) {
      onChange(name, value);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (onSubmit) {
      const submitData = {
        ...formData,
        eventTime: formData.eventTime ? `${formData.eventTime}:00` : "",
      };
      onSubmit(submitData);
    } else {
      console.error("onSubmit is not a function");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="manage-event-card-form">
      {console.log(formData)}
      <article
        className="manage-event-card"
        style={{
          width: formData.width + "px",
          height: formData.height + "px",
          fontSize: formData.fontSize + "px",
          backgroundImage: `linear-gradient(to top, rgba(0,0,0,1), rgba(0,0,0,.1)), url(${
            formData.backgroundImage ? formData.backgroundImage : formData.image
          })`,
          backgroundRepeat: "no-repeat",
          backgroundPosition: "center center",
          backgroundSize: "cover",
        }}
      >
        {imageBase64 ? (
          <img
            className="manage-event-avatar"
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
            className="manage-event-avatar"
            src={AvatarImage}
            width={100}
            height={100}
            alt="image_alt"
            style={{
              objectFit: "cover",
            }}
          />
        )}
        <div
          className="manage-card-content"
          style={{
            fontSize: formData.fontSize - 25 + "px",
            animation: `contentRightInAnim 0.9s ease-in-out`,
            color: `${isEdit ? "#000" : "#fff"}`,
          }}
        >
          <p className="manage-match-tag">Match</p>
          <div className="input-block">
            <input
              type="text"
              name="eventName"
              value={formData.eventName}
              onChange={handleChange}
              className={`manage-card-title manage-input ${
                isEdit ? "shift" : ""
              }`}
              readOnly={isEdit ? false : true}
            />
          </div>
          <div className="input-block">
            <p className="manage-attending-tag-event-card match-tag">
              Attending ({attendees ? attendees.length : ""})
            </p>
          </div>
          <div className="input-block">
            <div className="input-block-host-meta">
              <label>Host:</label>
              <div className="name-block">
                <p
                  type="text"
                  name="firstName"
                  className={`manage-card-firstname manage-input  ${
                    isEdit ? "" : ""
                  }`}
                >
                  {formData.firstName}
                </p>
                <p
                  type="text"
                  name="lastName"
                  className={`manage-card-lastname manage-input  ${
                    isEdit ? "" : ""
                  }`}
                >
                  {formData.lastName}
                </p>
              </div>
            </div>
          </div>
          <div className="input-block">
            <label className={` ${isEdit ? "marginreset" : ""}`}>
              Category:
            </label>
            <input
              type="text"
              name="category"
              value={formData.category}
              onChange={handleChange}
              className={`manage-card-category manage-input  ${
                isEdit ? "shift" : ""
              }`}
              readOnly={isEdit ? false : true}
            />
          </div>
          <div className="input-block">
            <div className="date-block-meta">
              <label className={` ${isEdit ? "marginreset" : ""}`}>Date:</label>
              <input
                type="date"
                name="eventDate"
                value={formData.eventDate}
                onChange={handleChange}
                className={`manage-card-date manage-input  ${
                  isEdit ? "shift" : ""
                }`}
                readOnly={isEdit ? false : true}
              />
              <input
                type="time"
                name="eventTime"
                value={formData.eventTime}
                onChange={handleChange}
                className={`manage-card-event-time manage-input  ${
                  isEdit ? "shift" : ""
                }`}
                readOnly={isEdit ? false : true}
              />
            </div>
          </div>
          <div className="input-block location-meta">
            <label className="location-label">Location:</label>
            <div className="location-details">
              <p>{`${location ? location + ", " : ""}${city} ${postalCode}`}</p>
            </div>
          </div>
          <div className="input-block manage-event-details-block">
            <label
              className={`manage-event-details-header ${
                isEdit ? "marginreset" : ""
              }`}
            >
              Event Details:
            </label>
            <textarea
              name="description"
              value={formData.description}
              onChange={handleChange}
              className={`manage-event-details-text manage-input  ${
                isEdit ? "shift" : ""
              }`}
              readOnly={isEdit ? false : true}
            />
          </div>
          <button type="submit">Update Event</button>
        </div>
      </article>
    </form>
  );
};

export default EventCardForm;
