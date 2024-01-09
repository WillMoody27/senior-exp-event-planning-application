import { useRef } from "react";
import React, { useState, useEffect } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimes } from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";
import { handleVerification } from "../../handlers/handler";
import "./Account.css";
import {
  fetchImageByUsername,
  fetchInterests,
  fetchUserInterests,
} from "../../handlers/api";
import placeholderImage from "../../assets/avatar-icon.svg";

// Global Constants
const BASEURL = "http://localhost:8080";

// Spinner Component
const Spinner = () => {
  return (
    <div
      style={{
        display: "inline-block",
        borderRadius: "10px",
        padding: "0.5rem 1rem",
        fontWeight: "800",
      }}
    >
      Updated Account Content...
    </div>
  );
};

const Account = () => {
  // VERIFICATION
  handleVerification();

  const [userEvents, setUserEvents] = useState([]);

  // NAVIGATE
  const navigate = useNavigate();

  // USE REFS, STATES, AND VARIABLES
  const fileInputRef = useRef(null);
  const [selectedInterests, setSelectedInterests] = useState([]);
  const [editDetails, setEditDetails] = useState(false);
  const [imageBase64, setImageBase64] = useState(null);
  const [availInterest, setAvailInterest] = useState([]);
  const [imageId, setImageId] = useState([0]);
  const [file, setFile] = useState(null);
  const [userData, setUserData] = useState([]);

  // CUSTOM HOOKS
  const [username, setUsername] = useState(
    `${localStorage.getItem("username")}`
  );
  const [profilePicture, setProfilePicture] = useState(
    "https://via.placeholder.com/150"
  );

  // Move later to commonElements.jsx
  const {
    firstName,
    lastName,
    jobDescription,
    nickname,
    phoneNumber,
    address,
    postalCode,
    password,
  } = userData;

  // METHOD CALLS HANDLERS
  const handleLogout = () => {
    // Clear user session or perform any other logout logic here
    localStorage.clear(); // Example: Clearing local storage

    // Redirect user to the login page or any other page
    navigate("/login"); // Update the path as per your routing setup
  };

  const handleImageView = () => {
    setTimeout(() => {
      fetch(`http://localhost:8080/images/${imageId[0]}`)
        .then((res) => res.json())
        .then((data) => {
          const base64String = data.image;
          setImageBase64(base64String);
        })
        .catch((error) => {
          console.log("Error Message: ", error);
        });
    }, 0);
  };
  const handleFetchUserData = () => {
    setTimeout(() => {
      fetch(`http://localhost:8080/api/users/${username}`)
        .then((res) => res.json())
        .then((userData) => {
          setUserData(userData);
          console.log(userData);
          localStorage.setItem("firstName", userData.firstName);
        });
    }, 0);
  };

  // Get the account image from the DB (From API.js)
  const fetchData = async () => {
    try {
      const userData = await fetchImageByUsername(username);
      console.log("User Data: ", userData);

      if (userData) {
        setImageId(userData);
      } else {
        // If userData is null or undefined, set a default image or handle it as needed
        setImageId(0); // Set a default image ID or handle it as you see fit
      }
    } catch (error) {
      // Handle any errors that occur during the fetch
      console.error("Error fetching user data:", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, [username]);

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };
  const handleUpload = () => {
    if (file) {
      const formData = new FormData();
      formData.append("file", file);

      fetch(`http://localhost:8080/images/uploadImage/${username}`, {
        method: "POST",
        body: formData,
      }).then((response) => {
        if (response.ok) {
          // Image uploaded successfully, you can handle the success here
          setProfilePicture(URL.createObjectURL(file));
        }
      });
    }
  };

  const handleChangeField = (e) => {
    const { name, value } = e.target;
    setUserData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };
  const handleSubmit = (e) => {
    e.preventDefault();

    // Create a FormData object
    const formData = new FormData();

    // Create an object of the form fields
    const createForm = {
      firstName: firstName,
      lastName: lastName,
      jobDescription: jobDescription,
      nickname: nickname,
      username: username,
      phoneNumber: phoneNumber,
      address: address,
      postalCode: postalCode,
      password: password,
      confirmPassword: password,
    };

    // Update the formData object
    formData.append("firstName", firstName);
    formData.append("lastName", lastName);
    formData.append("jobDescription", jobDescription);
    formData.append("nickname", nickname);
    formData.append("username", username);
    formData.append("phoneNumber", phoneNumber);
    formData.append("address", address);
    formData.append("postalCode", postalCode);
    formData.append("password", password);

    // Make a POST request to your backend endpoint
    fetch(
      `http://localhost:8080/api/users/${localStorage.getItem("username")}`,
      {
        method: "PUT",
        body: JSON.stringify(createForm),
        headers: {
          "Content-Type": "application/json",
        },
      }
    )
      .then((response) => {
        if (response.ok) {
          alert("Account Created");
        } else {
          // The request failed, handle the error here
          console.error("Failed to update user details");
          alert("User Details Not Updated 😭");
        }
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  };
  const fetchDataFromBackend = async () => {
    try {
      const response = await fetch(
        `${BASEURL}/api/interests/username/${username}`
      );
      const data = await response.json();
      // fetch the interests from the backend and show only that are not already selected.
      const fetchedInterests = data.map((item) => item.interest);
      setSelectedInterests(fetchedInterests);
    } catch (error) {
      console.error("Error fetching data from backend:", error);
    }
  };
  const handleDeleteImage = () => {
    fetch(`http://localhost:8080/images/deleteImage/${imageId}`, {
      method: "DELETE",
    })
      .then((response) => {
        response.ok ? setImageBase64(null) : console.log("Image Deleted");
      })
      .catch((error) => {
        console.error("Error removing image:", error);
      });
  };

  const handleGetUserEvents = async (e) => {
    // GET the events from the DB
    await fetch(`${BASEURL}/api/users/${username}`)
      .then((res) => res.json())
      .then((data) => {
        setUserEvents(data);
      })
      .catch((error) => {
        console.log("Error Message: ", error);
      });
  };

  // USE EFFECTS
  useEffect(() => {
    handleGetUserEvents();
  }, []);

  useEffect(() => {
    handleImageView();
  }, [imageId]);
  useEffect(() => {
    handleFetchUserData();
  }, []);
  useEffect(() => {
    fetchDataFromBackend();
  }, []);

  // INTERESTS
  const [userInterests, setUserInterests] = useState([]);

  const handleGetUserInterests = async () => {
    try {
      const data = await fetchUserInterests(username);
      if (data) {
        setUserInterests(data);
      }
    } catch (error) {
      console.log("Error Message: ", error);
    }
  };

  useEffect(() => {
    handleGetUserInterests();
  }, [username]);

  const [loading, setLoading] = useState(true); // State to track loading

  useEffect(() => {
    const fetchData = async () => {
      const data = await fetchInterests();
      setTimeout(() => {
        setAvailInterest(
          data.filter(
            (interest) =>
              !userInterests.find(
                (userInterest) => userInterest.interest === interest.interest
              )
          )
        );
        setLoading(false); // Update loading state when
      }, 1000); // Adjust based on the time it takes to fetch data as needed.
    };
    fetchData();
  }, [userInterests]);

  useEffect(() => {
    handleGetUserInterests();
  }, []);

  const handleSaveUserInterests = async (e, a_interest) => {
    console.log("ADDED: ", a_interest);

    let interest = a_interest.interest;
    let id = a_interest.id;

    e.preventDefault();
    // POST the interests to the DB
    await fetch(`${BASEURL}/api/users/${username}/interests/${interest}`, {
      method: "POST",
      body: JSON.stringify(interest),
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (response.ok) {
          console.log("Interest added successfully");

          setUserInterests([...userInterests, { id, interest }]);

          setAvailInterest(
            availInterest.filter((item) => item.interest !== interest)
          );
        }
      })
      .catch((error) => {
        console.log("Error:", error);
      });
  };

  const handleDeleteUserInterests = async (e, user_interests) => {
    console.log("DELETED: ", user_interests);

    let interest = user_interests.interest;
    let id = user_interests.id;

    e.preventDefault();
    // DELETE the interests from the DB
    await fetch(`${BASEURL}/api/users/${username}/interests/${id}`, {
      method: "DELETE",
    })
      .then((response) => {
        if (response.ok) {
          // update the user interests and avoid duplicates [Includes id and interest]
          setUserInterests(
            userInterests.filter((item) => item.interest !== interest)
          );

          // remove the interest from the available interests
          setAvailInterest([...availInterest, { id, interest }]);
        }
      })
      .catch((error) => {
        console.log("Error:", error);
      });
  };

  return (
    <div className="accounts-page">
      <div className="accounts-grid-block">
        <div className="grid-content">
          {/* User Profile Card Section */}

          {/* USER CONTAINER */}
          <div
            className={`user-profile-container ${editDetails ? "edit" : ""}`}
          >
            <div className="user-profile-card">
              <div className="user-profile-card-body">
                <div className="user-profile-card-body-content">
                  <div className="user-profile-card-body-content-image">
                    {/* Close Icon */}
                    <div className="image-profile-container">
                      {/* 
                      IMPORTANT CODE FOR DELETE ICON: If the image (imageBase64) is present then the delete icon will appear else it will not appear.
                       */}
                      {imageBase64 ? (
                        <FontAwesomeIcon
                          icon={faTimes}
                          className="close-icon"
                          data-testid="delete-icon"
                          onClick={(e) => {
                            handleDeleteImage(e);
                          }}
                        />
                      ) : (
                        <FontAwesomeIcon
                          style={{
                            display: "none",
                          }}
                          icon={faTimes}
                          className="close-icon"
                          data-testid="delete-icon"
                          onClick={(e) => {
                            handleDeleteImage(e);
                          }}
                        />
                      )}
                      {/* 
                      IMPORTANT CODE FOR IMAGE FIELD: If the image is present in DB then the image will appear else a placeholder image will appear in the size of 150x150.
                       */}
                      {imageBase64 ? (
                        <img
                          className="user-profile-image"
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
                          className="user-profile-image"
                          src={placeholderImage}
                          width={100}
                          height={100}
                          alt="image_alt"
                        />
                      )}
                    </div>
                    <input
                      type="file"
                      accept="image/*"
                      onChange={handleFileChange}
                      style={{ display: "none" }}
                      ref={fileInputRef}
                    />
                  </div>
                  {/* <input type="file" onChange={handleFileChange} /> */}
                  <div className="profile-img-btn-container">
                    <label htmlFor="fileInput" className="fileInputLabel">
                      Choose a file
                    </label>
                    <input
                      type="file"
                      id="fileInput"
                      className="fileInput"
                      onChange={handleFileChange}
                    />
                    <button
                      className="upload-image-button"
                      onClick={(e) => {
                        handleUpload(e);
                        // redirect to the same page
                        window.location.reload();
                      }}
                    >
                      Upload Image
                    </button>
                  </div>
                  <div className="user-profile-card-body-content-text">
                    <div className="meta-data">
                      <h3>{`${firstName ? firstName : ""} ${
                        lastName ? lastName : ""
                      }`}</h3>
                      <p>{jobDescription}</p>
                      <p>{nickname}</p>
                      <p>{username}</p>
                    </div>
                  </div>
                </div>
                <div className="logout-button-container">
                  <button onClick={handleLogout} className="logout-button">
                    Logout
                  </button>
                </div>
              </div>
            </div>

            {/* User Details Card Section */}

            <div
              className={`user-details-card
            ${editDetails ? "edit" : ""}`}
            >
              <div
                className={`user-details-card-header
                ${editDetails ? "" : "not-edit"}`}
              >
                <h2 className="edit-details-header">Manage Account Details</h2>
                <button
                  className={`edit-user-details-button ${
                    editDetails ? "" : "not-edit"
                  }`}
                  onClick={
                    editDetails
                      ? () => setEditDetails(false)
                      : () => setEditDetails(true)
                  }
                >
                  Edit User Details
                </button>
              </div>
              <div className="user-details-card-body">
                <div className="user-details-card-body-content">
                  <form
                    onSubmit={handleSubmit}
                    className="accounts-form-container"
                    style={{
                      display: editDetails ? "block" : "none",
                    }}
                  >
                    <div className="user-details-card-body-content-text">
                      <div>
                        <label htmlFor="firstName">First Name</label>
                        {/* Updated First and Last name fields to update localstorage */}
                        <input
                          type="text"
                          className={`${editDetails ? "shadow-true" : ""}`}
                          id="firstName"
                          name="firstName"
                          value={firstName}
                          onChange={(e) => {
                            handleChangeField(e);
                            localStorage.setItem("firstName", e.target.value);
                          }}
                        />
                        <label htmlFor="lastName">Last Name</label>
                        <input
                          type="text"
                          className={`${editDetails ? "shadow-true" : ""}`}
                          id="lastName"
                          name="lastName"
                          value={lastName}
                          onChange={(e) => {
                            handleChangeField(e);
                            localStorage.setItem("lastName", e.target.value);
                          }}
                        />
                        <label htmlFor="jobDescription">Job Description</label>
                        <input
                          type="text"
                          className={`${editDetails ? "shadow-true" : ""}`}
                          id="jobDescription"
                          name="jobDescription"
                          value={jobDescription}
                          onChange={handleChangeField}
                        />
                        <label htmlFor="nickname">Nickname</label>
                        <input
                          type="text"
                          className={`${editDetails ? "shadow-true" : ""}`}
                          id="nickname"
                          name="nickname"
                          value={nickname}
                          onChange={handleChangeField}
                        />
                        <label htmlFor="email">Email</label>
                        <input
                          type="email"
                          className={`${editDetails ? "shadow-true" : ""}`}
                          id="email"
                          name="username"
                          value={username}
                          onChange={handleChangeField}
                        />
                        <label htmlFor="phone">Phone</label>
                        <input
                          type="tel"
                          className={`${editDetails ? "shadow-true" : ""}`}
                          id="phone"
                          name="phoneNumber"
                          value={phoneNumber}
                          onChange={handleChangeField}
                        />
                        <label htmlFor="address">Address</label>
                        <input
                          type="text"
                          className={`${editDetails ? "shadow-true" : ""}`}
                          id="address"
                          name="address"
                          value={address}
                          onChange={handleChangeField}
                        />
                        <label htmlFor="postalCode">Postal Code</label>
                        <input
                          type="text"
                          className={`${editDetails ? "shadow-true" : ""}`}
                          id="postalCode"
                          name="postalCode"
                          value={postalCode}
                          onChange={handleChangeField}
                        />
                        <label htmlFor="password">Password</label>
                        <input
                          type="password"
                          className={`${editDetails ? "shadow-true" : ""}`}
                          id="password"
                          name="password"
                          value={password}
                          onChange={handleChangeField}
                        />
                        <label htmlFor="confirmPassword">
                          Confirm Password
                        </label>
                        <input
                          type="password"
                          className={`${editDetails ? "shadow-true" : ""}`}
                          id="confirmPassword"
                          name="confirmPassword"
                          value={password}
                          onChange={handleChangeField}
                        />
                      </div>

                      <button
                        className="submit-updated-user-details"
                        type="submit-updated-user-details"
                        style={{
                          display: editDetails ? "block" : "none",
                        }}
                        onClick={(e) => {
                          handleSubmit(e);
                          alert("Updated User Details");
                        }}
                      >
                        Update Account
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>

          {/* ✅ */}
          <div className="user-profile-lower-container">
            <div className="interests-card">
              {/* Added Custom Spinner [Not Implemented Yet 11-23-23] */}
              {loading ? (
                <Spinner />
              ) : (
                <div className="interests-wrap">
                  <h2 className="user-interest-text">Avail from DB</h2>
                  <div>
                    <div className="interest-wrapper">
                      <div
                        className="interest-text-block"
                        style={{
                          display: "flex",
                          alignItems: "center",
                          flexDirection: "row",
                          width: "70%",
                          flexWrap: "wrap",
                          justifyContent: "center",
                          margin: "0 auto",
                        }}
                      >
                        {/* Interests Text El's */}
                        {availInterest.map((a_interest) => (
                          <p
                            className="avail-interest-text"
                            style={{
                              padding: "0.5rem 1rem",
                              margin: "0.5rem",
                            }}
                            onClick={(e) => {
                              handleSaveUserInterests(e, a_interest);
                            }}
                            key={a_interest.id}
                          >
                            {a_interest.interest}
                          </p>
                        ))}
                        {/* Interests Text El's */}
                      </div>
                      <div className="interests-container-bubble">
                        {/* Dynamic Interest Here */}
                        <h2 className="user-interest-text">Your Interests</h2>
                        <div
                          className="interest-text-block"
                          style={{
                            display: "flex",
                            alignItems: "center",
                            flexDirection: "row",
                            width: "70%",
                            flexWrap: "wrap",
                            justifyContent: "center",
                            margin: "0 auto",
                          }}
                        >
                          {userInterests.map((userInterest) => (
                            <div key={userInterest.id}>
                              <p
                                style={{
                                  padding: "0.5rem 1rem",
                                  margin: "0.5rem",
                                  position: "relative",
                                }}
                                className="interest-text"
                                key={userInterest.id}
                              >
                                <FontAwesomeIcon
                                  icon={faTimes}
                                  className="interests-close-icon"
                                  onClick={(e) => {
                                    handleDeleteUserInterests(e, userInterest);
                                  }}
                                  key={userInterest.id}
                                />
                                {userInterest.interest}
                              </p>
                            </div>
                          ))}
                        </div>
                        {/* Dynamic Interest Here */}
                      </div>
                    </div>
                  </div>
                </div>
              )}
            </div>
            {/* <UserEventsCard/> */}
            <div className="events-card">
              <h2>Events</h2>
              <div className="events-signup-listing">
                {
                  // iterate over the array of events and display them. ? is used to check if the events array is empty or not.
                  userEvents.events?.map(
                    ({ eventName, eventId, eventDate, eventTime }) => (
                      <div className="user-account-events" key={eventId}>
                        <h3 className="account-event-name">{eventName}</h3>
                        <p className="account-event-name">
                          {new Date(eventDate).toLocaleDateString()}
                        </p>
                        <p className="account-event-name">{eventTime}</p>
                        <p className="registered-tag">Registered</p>
                      </div>
                    )
                  )
                }
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Account;
