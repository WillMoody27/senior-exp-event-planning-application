import React, { useState, useEffect, useRef } from "react";
import { Link } from "react-router-dom";
import "./CreateEvent.css";
import "../../components/event_card/EventCard.css";
import EventCard from "../event_card/EventCard";
import { handleVerification } from "../../handlers/handler";

const CreateEvent = () => {
  // Verification
  handleVerification();

  // Set the status of 200 or 400 for the notification
  const [responseStatus, setResponse] = useState("");
  const [showNotification, setShowNotification] = useState(false);

  const linkRef = useRef();

  useEffect(() => {
    if (showNotification) {
      const notificationTimeout = setTimeout(() => {
        setShowNotification(false);
      }, 2000);
      return () => {
        clearTimeout(notificationTimeout);
      };
    }
  }, [showNotification]);

  const [data, setData] = useState([]);
  useEffect(() => {
    // fetch data from the backend
    fetch("http://localhost:8080/api/events")
      .then((res) => res.json())
      .then((data) => {
        setData(data);
      });
  }, []);

  // GET FROM API ONCE AUTHENTICATION IS SET UP
  const user = {
    username: `${localStorage.getItem("username")}`,
    firstName: "",
    lastName: "",
  };

  const [notification, setNotification] = useState(false);
  const [notificationText, setNotificationText] = useState("");
  const fileInputRef = useRef(null);
  const [uploadedImageURL, setUploadedImageURL] = useState("");

  const [createForm, setCreateForm] = useState({
    eventName: "",
    username: `${localStorage.getItem("username")}`,
    eventDate: "",
    eventTime: "",
    location: "",
    postalCode: "",
    countryCode: "",
    city: "",
    description: "",
    category: "",
    image: "",
    imageFile: null,
    tags: [],
  });

  // append tags to the tags array
  const appendTags = (tag) => {
    setCreateForm({
      ...createForm,
      tags: [...createForm.tags, tag],
    });
  };

  const showAlert = () => {
    if (formReadyToSubmit) {
      setNotification(true);
      setTimeout(() => {
        setNotification(false);
      }, 2000);
    }
  };

  const handleChange = (e) => {
    setCreateForm({
      ...createForm,
      [e.target.name]: e.target.value,
    });
    console.log(createForm);
    // if for is empty, disable the submit button
    if (e.target.value === "") {
      setFormReadyToSubmit(false);
    } else {
      setFormReadyToSubmit(true);
    }
  };

  const [formReadyToSubmit, setFormReadyToSubmit] = useState(false);

  // Handling File Upload
  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setCreateForm({
      ...createForm,
      imageFile: file,
    });

    // Create a URL for the selected image file - Will create a temporary URL for the image as a blob, which can be used to preview the image.
    const imageURL = URL.createObjectURL(file);
    setUploadedImageURL(imageURL);
  };

  const handleUploadImage = () => {
    fileInputRef.current.click();
  };

  const [resText, setResText] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Create a FormData object
    const formData = new FormData();

    // Create an object of the form fields
    const { imageFile, ...formDataFields } = createForm;

    for (const key in formDataFields) {
      formData.append(key, formDataFields[key]);
    }

    // Append the imageFile separately
    formData.append("imageFile", createForm.imageFile);
    console.log("formData: ", formData);
    console.log(createForm);

    // Make a POST request to your backend endpoint
    fetch("http://localhost:8080/api/events", {
      method: "POST",
      body: JSON.stringify(createForm),
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (response.ok) {
          // The request was successful, you can handle the response here
          setResponse("Event created successfully");
          // Show the notification
          setShowNotification(true);
          linkRef.current.click(); // Redirect to home page
          // Reset the form or navigate to a different page
        } else {
          // The request failed, handle the error here
          setResponse("Failed to create event");
          // Show the notification
          setShowNotification(true);
          // Display an error message or handle the error as needed
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        // Handle any network errors here
      });
  };
  return (
    <section className="create-event-component">
      <Link to="/" ref={linkRef} style={{ display: "none" }}></Link>

      <h1 className="create-event-header">Create Event</h1>

      <div className="create-event-container">
        {/* Create event form */}
        <form className="create-event-form" onSubmit={handleSubmit}>
          {/* Background image for card */}
          <div className="form-block image-option-block">
            <label htmlFor="event-image">Image</label>
            <input
              type="text"
              name="image"
              id="event-image"
              placeholder="Image URL"
              onChange={handleChange}
            />
            {/* Upload image file */}
            <div className="upload-image-container">
              <input
                type="file"
                name="imageFile"
                id="event-image-file"
                accept="image/*"
                onChange={handleFileChange}
                ref={fileInputRef}
                style={{ display: "none" }}
              />
              <label
                htmlFor="event-image-file"
                className="file-upload-label"
                style={{
                  cursor: "pointer",
                  border: "1px solid black",
                  padding: "0.2rem 1rem",
                  marginTop: "0.5rem",
                  borderRadius: "10px",
                }}
              >
                Upload
              </label>
              <button
                style={{ display: "none", cursor: "pointer" }}
                className="upload-image-btn"
                onClick={handleUploadImage}
                type="button"
              >
                Upload
              </button>
            </div>
          </div>
          <div className="form-block">
            <label htmlFor="event-name">Event</label>
            <input
              type="text"
              name="eventName"
              id="event-name"
              data-testid="event-name"
              placeholder="Event Name"
              onChange={handleChange}
            />
          </div>
          <div className="form-block">
            <label htmlFor="event-date">Date</label>
            {/* DAY calendar input */}
            <input
              type="date"
              name="eventDate"
              id="event-date"
              data-testid="event-date"
              onChange={handleChange}
            />

            {/* YEAR */}
          </div>
          <div className="form-block">
            <label htmlFor="event-name">Time</label>
            <select
              name="eventTime"
              id="event-time"
              data-testid="event-time"
              className="event-date-select"
              onChange={handleChange}
            >
              {/* provide time format to match this 04:05:06 using 24hour format for morning and later times*/}
              <option value="10:00:00">10:00 AM</option>
              <option value="10:30:00">10:30 AM</option>
              <option value="11:00:00">11:00 AM</option>
              <option value="11:30:00">11:30 AM</option>
              <option value="12:00:00">12:00 PM</option>
              <option value="12:30:00">12:30 PM</option>
              <option value="13:00:00">1:00 PM</option>
              <option value="13:30:00">1:30 PM</option>
              <option value="14:00:00">2:00 PM</option>
              <option value="14:30:00">2:30 PM</option>
              <option value="15:00:00">3:00 PM</option>
              <option value="15:30:00">3:30 PM</option>
              <option value="16:00:00">4:00 PM</option>
              <option value="16:30:00">4:30 PM</option>
              <option value="17:00:00">5:00 PM</option>
              <option value="17:30:00">5:30 PM</option>
              <option value="18:00:00">6:00 PM</option>
              <option value="18:30:00">6:30 PM</option>
              <option value="19:00:00">7:00 PM</option>
              <option value="19:30:00">7:30 PM</option>
            </select>
          </div>
          <div className="address-block-container">
            <label htmlFor="event-location">Address</label>
            <div className="address-block">
              <input
                type="text"
                name="location"
                id="event-location"
                data-testid="event-address"
                placeholder="(e.g. 1234 Main St)"
                onChange={handleChange}
              />
              {/* City */}
              <input
                type="text"
                name="city"
                id="event-city"
                data-testid="event-city"
                placeholder="(e.g. Denver)"
                onChange={handleChange}
              />

              {/* ZIP */}
              <input
                type="text"
                name="postalCode"
                id="event-postal-code"
                data-testid="event-postal-code"
                placeholder="(e.g. 80201)"
                onChange={handleChange}
              />
            </div>
          </div>
          <div className="form-description-container">
            <label htmlFor="description">Description</label>
            <textarea
              name="description"
              id="description"
              data-testid="event-description"
              placeholder="Event Description"
              cols="30"
              rows="10"
              onChange={handleChange}
            ></textarea>
          </div>
          {/* Category */}
          <div className="form-block">
            <label htmlFor="event-duration">Category</label>
            <select
              name="category"
              id="event-category"
              data-testid="event-category"
              className="category-select"
              onChange={handleChange}
            >
              <option value="concert">Concert</option>
              <option value="sports">Sports</option>
              <option value="food">Food</option>
              <option value="outdoors">Outdoors</option>
              <option value="games">Games</option>
              <option value="other">Other</option>
            </select>
          </div>
          {/* Category */}
          {/* TAGS */}
          <div className="form-block">
            <label htmlFor="event-tags">Tags</label>
            <input
              type="text"
              name="tags"
              id="event-tags"
              data-testid="event-tags"
              placeholder="Add Tags"
              onKeyDown={(e) => {
                // restrict the tags to only 4 tags
                if (createForm.tags.length < 4) {
                  // Click tab to add tags
                  if (e.key === "Tab") {
                    appendTags(e.target.value);
                    e.target.value = "";
                  }
                  console.log("tags: ", createForm.tags);
                } else if (createForm.tags.length === 4) {
                  setNotificationText("Only 4 tags allowed");
                  showAlert();
                }
              }}
            />
          </div>
          {/* TAGS */}
          {/* Create Event Button */}
          {showNotification ? (
            <p
              className="notification-tag-text"
              style={{
                color: "#fff",
                backgroundColor: "#000",
                borderRadius: "10px",
                textAlign: "center",
                padding: "0.5rem 1rem",
                fontWeight: "900",
              }}
            >
              {responseStatus}
            </p>
          ) : null}
          <div className="form-block">
            <button
              className="submit-btn"
              data-testid="submit-btn"
              type="submit"
            >
              Create Event
            </button>
            <p
              data-testid="notification"
              className={`notification-tag-text
            ${notification ? "show" : ""}`}
            >
              {notificationText}
            </p>
          </div>
          <p className="caution-text">
            ⚠️ NOTE: Once this event is created a PDF will be accessible for a
            participants
          </p>
        </form>
        {/* Set Custom Fields for Create Event Card */}
        <EventCard
          {...createForm}
          user={user}
          backgroundImage={uploadedImageURL}
          width={600}
          height={600}
          fontSize={40}
        />
      </div>
    </section>
  );
};

export default CreateEvent;
