import React, { useState, useRef } from "react";
import { Link } from "react-router-dom";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faX } from "@fortawesome/free-solid-svg-icons";

const RegistrationOverlay2 = ({ onClose, onRegisterEmail }) => {
  const [notification, setNotification] = useState(false);
  const [notificationText, setNotificationText] = useState("");

  // For the redirect link after account creation
  const linkRef = useRef();

  /************************************************* */

  const [createForm, setCreateForm] = useState({
    firstName: "",
    lastName: "",
    username: "",
    password: "",
    passwordConfirm: "",
    role: "USER",
    provider: "LOCAL",
  });

  const showNotification = () => {
    if (formReadyToSubmit) {
      setNotification(true);
      setTimeout(() => {
        setNotification(false);
      }, 2000);
    }
  };

  // UPDATE FORM CONTENT FOR POST REQUEST
  const handleChange = (e) => {
    setCreateForm({
      ...createForm,
      [e.target.name]: e.target.value,
    });
    console.log(createForm);
  };

  const [formReadyToSubmit, setFormReadyToSubmit] = useState(false);

  // Create state to track whether overlay[Modal] is open or not
  const [isOverlayOpen, setIsOverlayOpen] = useState(false);
  // Arrow function to open the overlay
  const openOverlay = () => {
    setIsOverlayOpen(true);
  };
  // Arrow function to close the overlay
  const closeOverlay = () => {
    setIsOverlayOpen(false);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Create a FormData object
    const formData = new FormData();

    // Create an object of the form fields
    const { firstName, lastName, username, password, passwordConfirm } =
      createForm;

    // Update the formData object
    formData.append("firstName", firstName);
    formData.append("lastName", lastName);
    formData.append("username", username);
    formData.append("password", password);

    console.log(formData);

    if (password !== passwordConfirm) {
      alert("Passwords do not match");
      // clear password fields
      setCreateForm({
        ...createForm,
        password: "",
        passwordConfirm: "",
      });
      alert("Account NOT Created due to password mismatch😭");
      return;
    } else {
      // Make a POST request to your backend endpoint
      fetch("http://localhost:8080/api/users", {
        method: "POST",
        body: JSON.stringify(createForm),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((response) => {
          if (response.ok) {
            // The request was successful, you can handle the response here
            console.log("User created successfully");
            alert("Account Created");

            // Navigate to the close the overlay
            closeOverlay();
          } else {
            // The request failed, handle the error here
            console.error("Failed to create user");
            alert("Account NOT Created due to server problems😭");
            // Display an error message or handle the error as needed
          }
        })
        .catch((error) => {
          console.error("Error:", error);
          // Handle any network errors here
        });
    }
  };

  /************************************************* */
  // const handleSubmit = (event) => {
  //   event.preventDefault();
  //   console.log(email);
  // };

  return (
    <div className="overlay2">
      <div className="overlay2-content">
        <form
          className="overlay2-form"
          data-testid="overlay2-form"
          onSubmit={(e) => {
            handleSubmit(e);
          }}
        >
          {/* Close Button */}
          <FontAwesomeIcon
            icon={faX}
            className="overlay-close-button"
            onClick={onClose}
          />
          {/* Close Button */}

          <div className="create-close-btn-container">
            {/* <button className="overlay-back-button"> back </button>
            <button className="overlay-close-button" onClick={onClose}>
              {" "}
              close{" "}
            </button> */}
          </div>
          <h2 className="overlay-2-header"> Create An Account </h2>
          <div className="overlay-input-container">
            <label className="form-field-label" htmlFor="firstName">
              First Name
            </label>
            <input
              className="form-input-field"
              type="text"
              id="firstName"
              data-testid="first-name"
              name="firstName"
              placeholder="First Name"
              required
              onChange={(e) => {
                handleChange(e);
                localStorage.setItem("firstName", e.target.value);
              }}
            />
            <label className="form-field-label" htmlFor="lastName">
              {" "}
              Last Name{" "}
            </label>
            <input
              className="form-input-field"
              type="text"
              id="lastName"
              data-testid="last-name"
              name="lastName"
              placeholder="Last Name"
              required
              onChange={(e) => {
                handleChange(e);
                localStorage.setItem("lastName", e.target.value);
              }}
            />
            <label className="form-field-label" htmlFor="email">
              {" "}
              E-mail{" "}
            </label>
            <input
              className="form-input-field"
              type="text"
              id="email"
              data-testid="email"
              name="username"
              placeholder="E-mail"
              required
              onChange={(e) => {
                handleChange(e);
                localStorage.setItem("username", e.target.value);
              }}
            />
            <label className="form-field-label" htmlFor="password">
              {" "}
              Password{" "}
            </label>
            <input
              className="form-input-field"
              type="password"
              id="password"
              data-testid="password"
              name="password"
              placeholder="Password"
              required
              onChange={handleChange}
            />
            <label className="form-field-label" htmlFor="passwordConfirm">
              Confirm Password
            </label>
            <input
              className="form-input-field"
              type="password"
              id="passwordConfirm"
              data-testid="password-confirm"
              name="passwordConfirm"
              placeholder="Confirm Password"
              required
              onChange={handleChange}
            />
            <button
              className="register-button"
              type="submit"
              onClick={(e) => {
                handleSubmit(e);
              }}
            >
              Register
            </button>
          </div>
        </form>
        {/* Redirect Link After Account Creation */}
        <Link to="/account" ref={linkRef} style={{ display: "none" }}></Link>
        {/* End of Redirect Link After Account Creation */}
      </div>
    </div>
  );
};

export default RegistrationOverlay2;
