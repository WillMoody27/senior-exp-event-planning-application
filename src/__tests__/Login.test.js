import React from "react";
import { expect } from "chai";
import { render, fireEvent, screen } from "@testing-library/react";
import sinon from "sinon";
import { BrowserRouter } from "react-router-dom"; // Import BrowserRouter
import { act } from "react-dom/test-utils"; // Import 'act' for wrapping the fireEvent

import Login from "../components/login/Login";
import RegistrationOverlay2 from "../components/login/RegisterOverlay2";

describe("Login Component", () => {
  const wrapper = ({ children }) => <BrowserRouter>{children}</BrowserRouter>;

  let fetchStub;
  beforeEach(() => {
    fetchStub = sinon.stub(window, "fetch");
    fetchStub.resolves({
      ok: true,
      headers: new Headers({ Authorization: "Bearer some_token" }),
    });
  });

  afterEach(() => {
    // Restore the original fetch function
    fetchStub.restore();
  });

  it("should render without errors", () => {
    const { container } = render(<Login />, { wrapper });

    const loginDiv = container.querySelector(".login-div");
    expect(loginDiv).to.exist;
  });

  it("should handle email and password input changes", () => {
    const { getByTestId } = render(<Login />, { wrapper });
    const emailInput = getByTestId("email");
    const passwordInput = getByTestId("password");

    fireEvent.change(emailInput, { target: { value: "test@example.com" } });
    fireEvent.change(passwordInput, { target: { value: "password123" } });

    expect(emailInput.value).to.equal("test@example.com");
    expect(passwordInput.value).to.equal("password123");
  });

  it("handles the fetch call", async () => {
    const { getByTestId } = render(<Login />, { wrapper });

    // Fill in the form inputs
    const emailInput = getByTestId("email");
    const passwordInput = getByTestId("password");

    fireEvent.change(emailInput, { target: { value: "test@example.com" } });
    fireEvent.change(passwordInput, { target: { value: "password123" } });

    // Trigger form submission
    const form = getByTestId("login-form");
    fireEvent.submit(form);

    // Ensure that the preventDefault function is not called on the form submit event
    expect(fetchStub.called).to.be.true;

    // Ensure the mocked fetch function was called with the expected arguments
    expect(fetchStub.getCall(0).args[0]).to.contain("/api/users/login");
    expect(fetchStub.getCall(0).args[1].method).to.equal("POST");
    expect(fetchStub.getCall(0).args[1].body).to.equal(
      JSON.stringify({
        firstName: "",
        lastName: "",
        username: "test@example.com",
        password: "password123",
        passwordConfirm: "",
      })
    );
    expect(fetchStub.getCall(0).args[1].headers).to.deep.include({
      "Content-Type": "application/json",
    });
  });

  it("should open RegistrationOverlay2 when 'Create Account' button is clicked", () => {
    const { getByTestId } = render(<Login />, { wrapper });
    // const { getByText } = render(<RegistrationOverlay1 />);
    const createAccountButton = getByTestId("create-account-button");

    fireEvent.click(createAccountButton);

    expect(screen.queryByTestId("close-overlay-button")).to.exist;
  });

  it("should close RegistrationOverlay2 when onClose is called", () => {
    const { getByTestId } = render(<Login />, { wrapper });
    const createAccountButton = getByTestId("create-account-button");

    fireEvent.click(createAccountButton);

    const closeButton = screen.getByTestId("close-overlay-button");
    fireEvent.click(closeButton);

    expect(screen.queryByText("Register")).not.to.exist;
  });
});
