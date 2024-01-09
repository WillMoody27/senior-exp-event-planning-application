import React from "react";
import { Link } from "react-router-dom";

const SidebarLinkComponent = ({
  linkTo,
  linkClass,
  divOneClass,
  imageClass,
  imageIcon,
  imageAltText,
  pClass,
  pText,
  linkIconHeight,
}) => {
  return (
    <>
      <Link to={linkTo} className={linkClass}>
        <div className={divOneClass}>
          <img
            src={imageIcon}
            className={imageClass}
            alt={imageAltText}
            height={linkIconHeight}
          ></img>
          <p className={pClass}>{pText}</p>
        </div>
      </Link>
    </>
  );
};

export default SidebarLinkComponent;
