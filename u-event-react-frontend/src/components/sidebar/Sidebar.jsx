import React, { useState } from "react";
import "../../css/Sidebar.css";
import { Link } from "react-router-dom";
import SidebarAccountStatus from "./SidebarAccountStatus";
// Custom Icones Moved to CommonElements.jsx
import { linkContent } from "../commonElements";
// Custom Icones Moved to CommonElements.jsx
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBurger } from "@fortawesome/free-solid-svg-icons";

import LinkComponent from "./SidebarLinkComponent";

const Sidebar = () => {
  const [hideSidebar, setHideSidebar] = useState(false);
  const handleEventClick = () => {
    // Set Hide Sidebar To Opposite Of Current State
    setHideSidebar(!hideSidebar);
  };
  return (
    <div
      className={`sidebar-wrapper ${hideSidebar ? "show" : "hide"}`}
      data-testid="sidebar-wrapper"
    >
      <div className="sidebar-component-fill"></div>

      <div className={`sidebar ${hideSidebar ? "show" : ""}`}>
        <FontAwesomeIcon
          icon={faBurger}
          className={`burger-toggle`}
          data-testid="burger-toggle"
          onClick={(e) => {
            handleEventClick(e);
          }}
        />
        <h1 className={`sidebar-logo`}> UE</h1>
        <div className="hosted-section">
          {/* Link Components Clearner Code */}
          <LinkComponent {...linkContent[0]} />
          {/* Link Components Clearner Code */}
        </div>
        <div className="nav-section">
          {
            // IMPORTANT CODE: To iteate through the link components starting at the second index. To Edit the nav links, edit the linkContent array in commonElements.jsx
            linkContent.map((navlinkContent, index) => {
              if (index > 0) {
                return <LinkComponent {...navlinkContent} key={index} />;
              }
            })
          }
        </div>
        <Link to="/account" className="pill settings">
          <SidebarAccountStatus />
        </Link>
      </div>
    </div>
  );
};

export default Sidebar;
