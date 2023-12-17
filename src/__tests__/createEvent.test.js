import React from "react";
import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import CreateEvent from "../components/mainui/CreateEvent";
import { BrowserRouter } from "react-router-dom"; // Import BrowserRouter
// import sinon from "sinon"; // Add this import for Sinon
import { expect } from "chai"; // Add this import for Chai

describe("CreateEvent Component", () => {
  const wrapper = ({ children }) => <BrowserRouter>{children}</BrowserRouter>;

  it("should render without errors", () => {
    const { container } = render(<CreateEvent />, { wrapper });

    const createEventComponent = container.querySelector(
      ".create-event-component"
    );
    expect(createEventComponent).to.exist;
  });

  it("updates the form fields correctly", async () => {
    const { getByTestId } = render(<CreateEvent />, { wrapper });
    // Get form elements
    const eventName = getByTestId("event-name");
    const eventDate = getByTestId("event-date");
    const eventTime = getByTestId("event-time");
    const eventAddress = getByTestId("event-address");
    const eventCity = getByTestId("event-city");
    const eventPostalCode = getByTestId("event-postal-code");
    const eventDescription = getByTestId("event-description");
    const eventcategory = getByTestId("event-category");

    // Simulate user input
    fireEvent.change(eventName, { target: { value: "Test Event" } });
    fireEvent.change(eventDate, { target: { value: "2021-04-30" } });
    fireEvent.change(eventTime, { target: { value: "14:00:00" } });
    fireEvent.change(eventAddress, { target: { value: "Test Address" } });
    fireEvent.change(eventCity, { target: { value: "Test City" } });
    fireEvent.change(eventPostalCode, { target: { value: "00000" } });
    fireEvent.change(eventDescription, {
      target: { value: "Test Description" },
    });
    fireEvent.change(eventcategory, { target: { value: "food" } });

    // Assert that the form fields have been updated
    expect(eventName.value).to.equal("Test Event");
    expect(eventDate.value).to.equal("2021-04-30");
    expect(eventTime.value).to.equal("14:00:00");
    expect(eventAddress.value).to.equal("Test Address");
    expect(eventCity.value).to.equal("Test City");
    expect(eventPostalCode.value).to.equal("00000");
    expect(eventDescription.value).to.equal("Test Description");
    expect(eventcategory.value).to.equal("food");
  });

  it('displays a notification when the "Create Event" button is clicked', async () => {
    const { getByTestId } = render(<CreateEvent />, { wrapper });

    // Get the "Create Event" button by the data-testid attribute
    const createEventButton = getByTestId("submit-btn");

    // Click the "Create Event" button
    fireEvent.click(createEventButton);

    // Wait for the notification to appear - Meaning that the event has been created.

    const notification = screen.getByTestId("notification");
    expect(notification).to.exist; // Check the notification text
  });

  it('appends tags when the "Tab" key is pressed', () => {
    const { getByTestId } = render(<CreateEvent />, { wrapper });
    const eventTags = getByTestId("event-tags");

    fireEvent.change(eventTags, { target: { value: "Tag1" } });
    fireEvent.keyDown(eventTags, { key: "Tab", code: 9, charCode: 9 });

    expect(eventTags.value).to.equal("");
  });

  // it('disables the "Create Event" button when some fields are empty', () => {
  //   const { getByTestId } = render(<CreateEvent />, { wrapper });
  //   const createEventButton = getByTestId("submit-btn");

  //   const eventName = getByTestId("event-name");
  //   fireEvent.change(eventName, { target: { value: "Test Event" } });

  //   const eventDate = getByTestId("event-date");
  //   fireEvent.change(eventDate, { target: { value: "2021-04-30" } });

  //   // Add similar changes for other required fields

  //   expect(createEventButton).to.have.property("disabled", false);

  //   const eventTags = getByTestId("event-tags");
  //   fireEvent.change(eventTags, { target: { value: "Tag1" } });
  //   fireEvent.keyDown(eventTags, { key: "Tab", code: 9, charCode: 9 });

  //   expect(createEventButton).to.have.property("disabled", true);
  // });
});
