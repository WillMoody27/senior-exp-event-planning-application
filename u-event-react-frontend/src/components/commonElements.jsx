// Moved From Sidebar.jsx //
import calendarIcon from "../assets/calendar-icon.svg";
import dashboardIcon from "../assets/dashboard-icon.svg";
import messagesIcon from "../assets/messages-icon.svg";
import plusIcon from "../assets/plus-icon.svg";
import avatarImage from "../assets/avatar-icon.svg";
import manageEventsIcon from "../assets/check_circle_outline_24px_rounded.svg";

// Avatar image black and white image
export const AvatarImage = avatarImage;

// For the Navigation Menu, which includes links and icons
// This export will include the entire link component and its props
export const linkContent = [
  {
    linkTo: "/create-events",
    linkClass: "",
    divOneClass: "create-event",
    imageClass: "create-event-btn",
    imageIcon: plusIcon,
    imageAltText: "plus icon",
    pClass: "create-event-text",
    pText: "Create",
    linkIconHeight: "100%",
  },
  {
    linkTo: "/",
    linkClass: "pill nav",
    divOneClass: "sidebar-icon-container",
    imageClass: "sidebar-icons",
    imageIcon: dashboardIcon,
    imageAltText: "messages icon",
    pClass: "link-text",
    pText: "Dashboard",
    linkIconHeight: 20,
  },
  {
    linkTo: "/messages",
    linkClass: "pill nav",
    divOneClass: "sidebar-icon-container",
    imageClass: "sidebar-icons",
    imageIcon: messagesIcon,
    imageAltText: "dashboard icon",
    pClass: "link-text",
    pText: "Messages",
    linkIconHeight: 20,
  },
  {
    linkTo: "/calendar",
    linkClass: "pill nav",
    divOneClass: "sidebar-icon-container",
    imageClass: "sidebar-icons",
    imageIcon: calendarIcon,
    imageAltText: "calendar icon",
    pClass: "link-text",
    pText: "Calendar",
    linkIconHeight: 25,
  },

  {
    linkTo: "/manage-events",
    linkClass: "pill nav",
    divOneClass: "sidebar-icon-container",
    imageClass: "sidebar-icons",
    imageIcon: manageEventsIcon,
    imageAltText: "manage events icon",
    pClass: "link-text",
    pText: "Manage",
    linkIconHeight: 25,
  },
];
