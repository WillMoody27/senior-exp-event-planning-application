import React from "react";
import { expect } from "chai";
import { render, fireEvent, screen } from "@testing-library/react";
import sinon from "sinon";
import { BrowserRouter } from "react-router-dom";
import RegisterOverlay2 from "../components/login/RegisterOverlay2";

describe("RegisterOverlay2 Component", () => {
  const wrapper = ({ children }) => <BrowserRouter>{children}</BrowserRouter>;

  it("should render without errors", () => {
    const { container } = render(<RegisterOverlay2 />, { wrapper });

    const overlay = container.querySelector(".overlay2");
    expect(overlay).to.exist;
  });

  it("should handle input changes", () => {
    const { getByTestId } = render(<RegisterOverlay2 />, { wrapper });
    const firstNameInput = getByTestId("first-name");
    const lastNameInput = getByTestId("last-name");
    const emailInput = getByTestId("email");
    const passwordInput = getByTestId("password");
    const passwordConfirmInput = getByTestId("password-confirm");

    fireEvent.change(firstNameInput, { target: { value: "John" } });
    fireEvent.change(lastNameInput, { target: { value: "Doe" } });
    fireEvent.change(emailInput, { target: { value: "john@example.com" } });
    fireEvent.change(passwordInput, { target: { value: "password123" } });
    fireEvent.change(passwordConfirmInput, {
      target: { value: "password123" },
    });

    expect(firstNameInput.value).to.equal("John");
    expect(lastNameInput.value).to.equal("Doe");
    expect(emailInput.value).to.equal("john@example.com");
    expect(passwordInput.value).to.equal("password123");
    expect(passwordConfirmInput.value).to.equal("password123");
  });

  // it("should call handleSubmit when the form is submitted", () => {
  //   // Create a spy function to replace the actual handleSubmit function
  //   const handleSubmitSpy = jest.spyOn(RegisterOverlay2.prototype, "handleSubmit");
  //   const onClose = jest.fn(); // Mock the onClose function

  //   const { getByTestId } = render(<RegisterOverlay2 onClose={onClose} />, {
  //     wrapper,
  //   });

  //   const form = getByTestId("overlay2-form");

  //   fireEvent.submit(form);

  //   // Assert that the spy function was called
  //   expect(handleSubmitSpy).toHaveBeenCalled();

  //   // Restore the original handleSubmit method
  //   handleSubmitSpy.mockRestore();
  // });

  it("should close RegisterOverlay2 when onClose is called", () => {
    const onClose = sinon.stub();

    const { getByTestId } = render(<RegisterOverlay2 onClose={onClose} />, {
      wrapper,
    });

    const closeButton = getByTestId("close-overlay-button");

    fireEvent.click(closeButton);

    expect(onClose.calledOnce).to.be.true;
  });
});
