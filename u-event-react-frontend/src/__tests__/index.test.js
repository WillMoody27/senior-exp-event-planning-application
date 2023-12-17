import React from "react";
import { expect } from "chai";
import sinon from "sinon";
import ReactDOM from "react-dom/client";
import App from "../App";

describe("index.js", () => {
  let createRootStub;
  let renderStub;
  let div;

  beforeEach(() => {
    // Create a new div element and append it to the document body
    div = document.createElement("div");
    div.id = "root";
    document.body.appendChild(div);

    // Stub the createRoot method
    renderStub = sinon.stub();
    createRootStub = sinon
      .stub(ReactDOM, "createRoot")
      .returns({ render: renderStub });
  });

  afterEach(() => {
    // Restore the original functionality and clean up the DOM
    createRootStub.restore();
    document.body.removeChild(div);
  });

  it("renders without crashing", () => {
    // Dynamically import the index.js file to execute the code within it
    require("../index");

    // Check if the createRoot and render methods were called
    sinon.assert.calledWith(createRootStub, div);
    sinon.assert.calledWith(
      renderStub,
      <React.StrictMode>
        <App />
      </React.StrictMode>
    );
  });
});
