import React from "react";
import { render, fireEvent, screen, getByTestId } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom"; // Import BrowserRouter
import Sidebar from "../components/sidebar/Sidebar";
import "../css/Sidebar.css";

describe("Sidebar Component", () => {
  const wrapper = ({ children }) => <BrowserRouter>{children}</BrowserRouter>;

  it("should render without errors", () => {
    const { container } = render(<Sidebar />, { wrapper });
  });

  it("should initially hide the sidebar", () => {
    const { getByTestId } = render(<Sidebar />, { wrapper });

    // Initially, the sidebar is hidden
    const sidebarWrapper = getByTestId("sidebar-wrapper");
    expect(sidebarWrapper.classList.contains("hide")).toBe(true);
  });

  it("should toggle sidebar on burger icon click", () => {
    const { getByTestId } = render(<Sidebar />, { wrapper });
    const burgerIcon = getByTestId("burger-toggle");
    const sidebarWrapper = getByTestId("sidebar-wrapper");
    fireEvent.click(burgerIcon);
    expect(sidebarWrapper.classList.contains("hide")).toBe(false);
  });

  it("should have a link to Dashboard", () => {
    const { getByText } = render(<Sidebar />, { wrapper });
    const dashboardLink = getByText("Dashboard");

    expect(dashboardLink).toBeTruthy();
  });

  it("should have a link to Messages", () => {
    const { getByText } = render(<Sidebar />, { wrapper });
    const messagesLink = getByText("Messages");

    expect(messagesLink).toBeTruthy();
  });

  it("should have a link to Calendar", () => {
    const { getByText } = render(<Sidebar />, { wrapper });
    const calendarLink = getByText("Calendar");

    expect(calendarLink).toBeTruthy();
  });

  it("should have a link to Account", () => {
    const { getByText } = render(<Sidebar />, { wrapper });
    const accountLink = getByText("Account");

    expect(accountLink).toBeTruthy();
  });
});
