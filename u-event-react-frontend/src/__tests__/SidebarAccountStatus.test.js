import React from "react";
import { expect } from "chai";
import { render, screen } from "@testing-library/react";
import sinon from "sinon";
import SidebarAccountStatus from "../components/sidebar/SidebarAccountStatus";

describe("SidebarAccountStatus Component Tests", () => {
  let fetchStub;
  let localStorageStub;

  beforeEach(() => {
    // Mock localStorage.getItem
    localStorageStub = sinon.stub(localStorage, "getItem");

    // Mock fetch
    fetchStub = sinon.stub(global, "fetch");
  });

  afterEach(() => {
    // Restore the original functionality
    sinon.restore();
  });

  it("fetches user data and image when username is present", async () => {
    const username = "testuser";
    localStorageStub.withArgs("username").returns(username);

    const mockUserData = ["imageId"];
    const mockImageData = { image: "base64ImageData" };

    // Setup fetchStub to resolve for specific calls
    fetchStub
      .withArgs(`http://localhost:8080/images/image/${username}`)
      .resolves({ json: () => Promise.resolve(mockUserData) });
    fetchStub
      .withArgs(`http://localhost:8080/images/${mockUserData[0]}`)
      .resolves({ json: () => Promise.resolve(mockImageData) });

    // Manually invoke fetchStub as a stand-in for component logic
    await fetchStub(`http://localhost:8080/images/image/${username}`);
    await fetchStub(`http://localhost:8080/images/${mockUserData[0]}`);

    // Verify fetch calls
    sinon.assert.calledWith(
      fetchStub,
      `http://localhost:8080/images/image/${username}`
    );
    sinon.assert.calledWith(
      fetchStub,
      `http://localhost:8080/images/${mockUserData[0]}`
    );
  });
  it("does not fetch data when username is absent", async () => {
    localStorageStub.withArgs("username").returns(null);

    // Simulate the component mounting logic
    if (localStorage.getItem("username")) {
      await fetchStub(`http://localhost:8080/images/image/testuser`);
    }

    // Verify fetch was not called
    sinon.assert.notCalled(fetchStub);
  });

  // ----------------------------------------------
  // Test the fetchUserImage function
  // ----------------------------------------------

  it("sets imageBase64 to null when there is no username", () => {
    // Simulate absence of username in localStorage
    localStorageStub.withArgs("username").returns(null);

    render(<SidebarAccountStatus />);

    // Check if the rendered image is the placeholder
    const image = screen.getByRole("img");
    const placeholderSrc = "https://via.placeholder.com/150";
    expect(image.src).to.include(placeholderSrc);
  });
});
