import React from "react";
import { render } from "@testing-library/react";
import MainUI from "../components/mainui/MainUI";
import { BrowserRouter } from "react-router-dom"; // Import BrowserRouter
import EventBoard from "../components/mainui/EventBoard"; // Update the import path accordingly

describe("MainUI Component", () => {
  const wrapper = ({ children }) => <BrowserRouter>{children}</BrowserRouter>;

  it("should render without errors", () => {
    // Render the MainUI component
    const { getByTestId } = render(<MainUI />, { wrapper });

    // Ensure that the EventBoard component is rendered within MainUI
    const eventBoardComponent = getByTestId("event-board");
    expect(eventBoardComponent).toBeInTheDocument();
  });
});
