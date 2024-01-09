import React, { useEffect, useState } from "react";
import "./SidebarAccountStatus.css";
import { fetchUserImage } from "../../../handlers/api";

const SidebarAccountStatus = () => {
  const [imageBase64, setImageBase64] = useState(null);
  const username = localStorage.getItem("username");

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
            alt="account_image"
          />
        ) : (
          <img
            src="https://via.placeholder.com/150"
            className="avatar no-avatar"
            alt="placeholder_image"
          />
        )}
      </div>
    </div>
  );
};

export default SidebarAccountStatus;
