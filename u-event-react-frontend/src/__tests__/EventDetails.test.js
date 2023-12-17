import React from "react";
import { render, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import sinon from "sinon";
import { expect } from "chai";
import EventDetails from "../components/mainui/EventDetails";

describe("<EventDetails />", () => {
  // ... Setup for stubs ...
  let fetchStub;

  const mockEvent = {
    eventId: "123",
    eventName: "Mock Event",
    user: {
      username: "mockUser",
      firstName: "John",
      lastName: "Doe",
    },
    eventDate: "2023-07-20T18:00:00Z", // Example date in ISO format
    location: "Mock Location",
    description: "This is a mock event description.",
    image: "https://example.com/mock-image.jpg",
    tags: ["tag1", "tag2", "tag3"],
    attendees: ["user1", "user2", "user3"],
  };
  
  beforeEach(() => {
    fetchStub = sinon.stub(window, 'fetch');
  });

  afterEach(() => {
    sinon.restore();
  });

  it("renders without crashing", () => {
    const { getAllByText, getByTestId } = render(
      <MemoryRouter initialEntries={[{ state: { event: mockEvent } }]}>
        <EventDetails />
      </MemoryRouter>
    );
    const allInstances = getAllByText("Mock Event");
    const eventDetails = getByTestId("event-detail");
    expect(eventDetails).to.exist;
    expect(allInstances.length).to.be.greaterThan(0);
  });
  

  it("formats date correctly", async () => {
    const { getByText } = render(
      <MemoryRouter initialEntries={[{ state: { event: mockEvent } }]}>
        <EventDetails />
      </MemoryRouter>
    );
  
    const dateRegex = /Thursday, July 20, 2023[\s\S]*12:00 PM/;
    
    await waitFor(() => {
      expect(getByText(dateRegex)).to.be.ok;
    });
  });

  it("handles join event correctly", async () => {
    const response = new Response(JSON.stringify({}), { status: 200 });
    fetchStub.resolves(response);

    const { getByText } = render(
      <MemoryRouter initialEntries={[{ state: { event: mockEvent } }]}>
        <EventDetails />
      </MemoryRouter>
    );

    const joinButton = getByText("Join");
    fireEvent.click(joinButton);

    // Since the state update could be asynchronous, wait for the button text to change
    await waitFor(() => {
      expect(getByText("Joined")).to.be.ok;
    });
  });
  
});
