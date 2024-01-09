import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link } from "react-router-dom";
import {
  faInstagram,
  faFacebook,
  faTwitter,
} from "@fortawesome/free-brands-svg-icons";
import "./Footer.css";

const Footer = () => {
  return (
    <div className="footer-sec">
      <div className="footer-content">
        <div className="footer-logo">
          <Link to="/" className="logo-content">
            <h1 className="brand-name">U-Event</h1>
          </Link>
        </div>
        <div className="footer-links">
          <ul>
            <Link to={"/"}>Explore</Link>
            <Link to={"/account"}>Account</Link>
            <Link to={"/manage-events"}>Manage Events</Link>
          </ul>
        </div>
        <div className="footer-social-media">
          <ul>
            <li>
              <FontAwesomeIcon icon={faInstagram} />
            </li>
            <li>
              <FontAwesomeIcon icon={faFacebook} />
            </li>
            <li>
              <FontAwesomeIcon icon={faTwitter} />
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default Footer;
