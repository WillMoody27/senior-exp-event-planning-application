import React from "react";
import { render, screen } from "@testing-library/react";
import App from "../App";

// Mock child components
jest.mock("../components/mainui/Eventboard", () => () => (
  <section>EventBoard</section>
));
jest.mock("../components/sidebar/Sidebar", () => () => <div>Sidebar</div>);
jest.mock("../components/Account/Account", () => () => <div>Account</div>);
jest.mock("../components/mainui/EventDetails", () => () => (
  <div>EventDetails</div>
));
jest.mock("../components/mainui/CreateEvent", () => () => (
  <div>CreateEvent</div>
));
jest.mock("../components/mainui/ManageEvents", () => () => (
  <div>ManageEvents</div>
));

describe("App Component", () => {
  test("renders App and child components correctly", () => {
    render(<App />);

    // Using Jest's matchers
    expect(screen.getByText("MainUI")).toBeInTheDocument();
    // Add assertions for other components as necessary
  });
});
