import React, { useState, useRef } from "react";
import { Link } from "react-router-dom";
// import RegistrationOverlay1 from "./RegisterOverlay1";
import RegistrationOverlay2 from "./RegisterOverlay2";
import "../../css/Login.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faApple,
  faFacebook,
  faGoogle,
} from "@fortawesome/free-brands-svg-icons";
import loginimage from "../../assets/ugur-arpaci-U18V0ToioFU-unsplash.jpg";

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  // For the token
  const [token, setToken] = useState("");
  // For the token

  const linkRef = useRef();

  const [createForm, setCreateForm] = useState({
    firstName: "",
    lastName: "",
    username: "",
    password: "",
  });

  const handleChange = (e) => {
    setCreateForm({
      ...createForm,
      [e.target.name]: e.target.value,
    });
    console.log(createForm);
  };

  // handle Login

  // "http://localhost:8080/api/users/login"

  // const handleLogin = (e) => {
  //   e.preventDefault();

  //   const { username, password } = this.state;
  //   //send login data to backend using fetchAPI
  //   fetch("http://localhost:8080/api/users/login", {
  //     method: "POST",
  //     headers: {
  //       "Content-Type": "application/json",
  //     },
  //     body: JSON.stringify({ username, password }),
  //   })
  //     .then((response) => {
  //       if (response.ok) {
  //         // The request was successful, you can handle the response here
  //         console.log("Event created successfully");
  //         // Reset the form or navigate to a different page
  //       } else {
  //         // The request failed, handle the error here
  //         console.error("Failed to create event");
  //         // Display an error message or handle the error as needed
  //       }
  //     })
  //     .catch((error) => {
  //       console.error("Error:", error);
  //       // Handle any network errors here
  //     });
  // };

  const handlesubmit = async (e) => {
    e.preventDefault();

    // Create a FormData object
    const formData = new FormData();

    // Create an object of the form fields
    const { ...formDataFields } = createForm;

    for (const key in formDataFields) {
      formData.append(key, formDataFields[key]);
    }

    console.log("formData: ", formData);
    console.log(createForm);

    // Make a POST request to your backend endpoint
    fetch("http://localhost:8080/api/users/login", {
      method: "POST",
      body: JSON.stringify(createForm),
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (response.ok) {
          // The request was successful, you can handle the response here
          console.log("Logged In successfully");
          // ******** GET JWT TOKEN ********
          fetch(
            `http://localhost:8080/api/users/${localStorage.getItem(
              "username"
            )}`,
            {
              method: "GET",
              headers: {
                "Content-Type": "application/json",
              },
            }
          ).then((tokenData) => {
            console.log("Token Data: ", tokenData);
            localStorage.setItem("token", tokenData);
          });
          // ******** GET JWT TOKEN ********

          // Reset the form or navigate to a different page
          linkRef.current.click();
        } else {
          // The request failed, handle the error here
          console.error("Failed to log user in");
          // Display an error message or handle the error as needed
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        // Handle any network errors here
      });
  };

  // Create state to track whether overlay[Modal] is open or not
  const [isOverlayOpen, setIsOverlayOpen] = useState(false);
  // Arrow function to open the overlay
  const openOverlay = () => {
    console.log("openOverlay");
    setIsOverlayOpen(true);
  };
  // Arrow function to close the overlay
  const closeOverlay = () => {
    console.log("closeOverlay");
    setIsOverlayOpen(false);
  };

  return (
    <div className="login-div">
      <div className="show-overlay-container">
        {/*
            // Boolean expression to check if overlay is open or not
        */}
        {isOverlayOpen && (
          <RegistrationOverlay2 onClose={closeOverlay} onSelectMethod />
        )}
      </div>

      <div className="login-screen-container">
        <div className="login-email-container">
          <div className="form-container">
            <form
              className="login-form"
              data-testid="login-form"
              onSubmit={handlesubmit}
            >
              <h2 className="brand-logo">U-Event</h2>
              <label htmlFor="email"> E-mail </label>
              <input
                type="text"
                id="email"
                data-testid="email"
                name="username"
                required
                onChange={(e) => {
                  handleChange(e);
                  localStorage.setItem("username", e.target.value);
                  // Reset firstName and lastName from localStorage
                  localStorage.removeItem("firstName");
                  localStorage.removeItem("lastName");
                }}
              />
              <label htmlFor="password"> Password </label>
              <input
                type="password"
                id="password"
                data-testid="password"
                name="password"
                required
                onChange={handleChange}
              />
              <button className="login-button" type="submit">
                {" "}
                Login{" "}
              </button>
              <button className="link-button" type="Forgot Password">
                {" "}
                Forgot Password{" "}
              </button>
              <button
                className="create-button"
                data-testid="create-account-button"
                type="Create Account"
                onClick={openOverlay}
              >
                Don't have an account! Create one here
              </button>
              {/* Auth Provider Buttons */}
              <div className="divider">
                <p className="custom-or-text">or</p>
                <hr />
              </div>
              <div className="button-container">
                <a
                  href="/"
                  className="google-btn"
                  onClick={(e) => {
                    e.preventDefault();
                  }}
                >
                  <FontAwesomeIcon className="btn-icon" icon={faGoogle} />
                </a>
                <a
                  href="/"
                  className="facebook-btn"
                  onClick={(e) => {
                    e.preventDefault();
                  }}
                >
                  <FontAwesomeIcon className="btn-icon" icon={faFacebook} />
                </a>
                <a
                  href="/"
                  className="google-btn"
                  onClick={(e) => {
                    e.preventDefault();
                  }}
                >
                  <FontAwesomeIcon className="btn-icon" icon={faApple} />
                </a>
              </div>
              {/* Auth Provider Buttons */}
            </form>
            <Link to="/" ref={linkRef} style={{ display: "none" }}></Link>
            <div className="login-image-container">
              {/* IMAGE */}

              {/* IMAGE */}
            </div>
          </div>
        </div>
      </div>

      <div>
        {username}
        {password}
      </div>
    </div>
  );
};

export default Login;
