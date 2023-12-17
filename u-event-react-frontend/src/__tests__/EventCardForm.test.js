import React from "react";
import { render, fireEvent } from "@testing-library/react";
import { expect } from "chai";
import sinon from "sinon";
import EventCardForm from "../components/mainui/EventCardForm";


describe("<EventCardForm />", () => {
    it("renders correctly and allows input changes", () => {
      const mockSubmit = sinon.spy();
      const mockChange = sinon.spy();
  
      // Mock user data
      const mockUser = {
        firstName: "John",
        lastName: "Doe",
      };
  
      const { getByLabelText, getByText } = render(
        <EventCardForm
          isEdit={true}
          onSubmit={mockSubmit}
          onChange={mockChange}
          editing={false}
          user={mockUser}
        />
      );
  
      const eventNameInput = getByLabelText("Event Name:");
      fireEvent.change(eventNameInput, { target: { value: "New Event Name" } });
  
      expect(eventNameInput.value).to.equal("New Event Name");
      expect(mockChange.calledOnce).to.be.true;
  
      fireEvent.click(getByText("Update Event"));
      expect(mockSubmit.calledOnce).to.be.true;
    });
  
    // More tests...
  });
  
  