import dashboardIcon from "../assets/dashboard-icon.svg";
import plusIcon from "../assets/plus-icon.svg";
import avatarImage from "../assets/avatar-icon.svg";
import manageEventsIcon from "../assets/check_circle_outline_24px_rounded.svg";

export const AvatarImage = avatarImage;

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
