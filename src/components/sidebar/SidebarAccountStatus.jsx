import React, { useEffect, useState } from "react";
import "../../css/SidebarAccountStatus.css";
import { fetchUserImage } from "../../handlers/api"; // Import the function

const SidebarAccountStatus = () => {
  const [imageBase64, setImageBase64] = useState(null);
  const username = localStorage.getItem("username");

  // TODO-Complete: Combined endpoint to fetch user data and image from the backend
  useEffect(() => {
    fetchUserImage(username)
      .then(setImageBase64)
      .catch((error) => {
        setImageBase64(null);
      });
  }, [username]);

  return (
    <div className="sidebar-account-status">
      <div className="sidebar-account-status-container">
        {imageBase64 != null ? (
          <img
            src={`data:image/jpeg;base64,${imageBase64}`}
            className="avatar"
            width={100}
            height={100}
            alt="account_image"
          />
        ) : (
          <img
            src="https://via.placeholder.com/150"
            className="avatar no-avatar"
            width={100}
            height={100}
            alt="placeholder_image"
          />
        )}
        <p className="sidebar-account-status-name">Account</p>
      </div>
    </div>
  );
};

export default SidebarAccountStatus;
