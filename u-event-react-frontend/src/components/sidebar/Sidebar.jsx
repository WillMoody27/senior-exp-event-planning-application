import React, { useState } from "react";
import { Link } from "react-router-dom";
// CSS
import "./Sidebar.css";
// Components
import SidebarAccountStatus from "../../components/sidebar/sidebar_account_status/SidebarAccountStatus";
import LinkComponent from "../../components/sidebar/sidebar_account_status/SidebarLinkComponent";
// Data
import { linkContent } from "../commonElements";
// Font Awesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars } from "@fortawesome/free-solid-svg-icons";

const Sidebar = () => {
  const [hideSidebar, setHideSidebar] = useState(false);
  const handleEventClick = () => {
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
          icon={faBars}
          className={`toggle`}
          data-testid="toggle"
          onClick={(e) => {
            handleEventClick(e);
          }}
        />
        <h1 className={`sidebar-logo`}> UE</h1>
        <div className="hosted-section">
          <LinkComponent {...linkContent[0]} />
        </div>
        <div className="nav-section">
          {linkContent.map((navlinkContent, index) => {
            if (index > 0) {
              return <LinkComponent {...navlinkContent} key={index} />;
            }
          })}
        </div>
        <Link to="/account" className="pill settings">
          <SidebarAccountStatus />
        </Link>
      </div>
    </div>
  );
};

export default Sidebar;
