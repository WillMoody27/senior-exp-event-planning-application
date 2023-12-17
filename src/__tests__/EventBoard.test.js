import React from "react";
import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import EventBoard from "../components/mainui/EventBoard";
import { BrowserRouter } from "react-router-dom"; // Import BrowserRouter
// import sinon from "sinon"; // Add this import for Sinon
import { expect } from "chai"; // Add this import for Chai

describe("EventBoard Component", () => {
  const wrapper = ({ children }) => <BrowserRouter>{children}</BrowserRouter>;

  it("should render without errors", () => {
    const { getByTestId } = render(<EventBoard />, { wrapper });

    // Ensure that the EventBoard component is rendered within MainUI
    const eventBoardComponent = getByTestId("event-board");
    expect(eventBoardComponent).to.exist;
  });

  it("should handle search bar input changes", () => {
    const { getByTestId } = render(<EventBoard />, { wrapper });
    const searchInput = getByTestId("search-bar");

    fireEvent.change(searchInput, { target: { value: "search" } });
    expect(searchInput.value).to.equal("search");
  });

  it("should handle filter input changes", () => {
    const { getByTestId } = render(<EventBoard />, { wrapper });
    // const filterInput = getByTestId("event-board-filter");

    const select = getByTestId("event-board-filter");
    fireEvent.change(select, { target: { value: "concert" } });
    expect(select.value).to.equal("concert");
  });
});
