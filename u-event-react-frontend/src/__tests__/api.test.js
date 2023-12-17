import { expect } from "chai";
import sinon from "sinon";
import {
  fetchEvents,
  fetchUserImage,
  fetchImageData,
  fetchUserDetails,
  fetchImageId,
  fetchAndSetImageBase64,
  fetchImageByUsername,
  fetchInterests,
  fetchUserInterests,
} from "../handlers/api"; // Adjust the path as per your project structure

describe("API", () => {
  let fetchStub;

  beforeEach(() => {
    // Stub the global fetch function
    fetchStub = sinon.stub(global, "fetch");
  });

  afterEach(() => {
    // Restore the original fetch function
    fetchStub.restore();
  });

  it("fetchEvents should return a list of events on successful fetch", async () => {
    // Mock the fetch response
    fetchStub.resolves(
      new Response(JSON.stringify([{ id: 1, name: "Event 1" }]), {
        status: 200,
        headers: { "Content-Type": "application/json" },
      })
    );

    const events = await fetchEvents();
    expect(events).to.be.an("array");
    expect(events).to.have.length(1);
    expect(events[0]).to.deep.equal({ id: 1, name: "Event 1" });
  });

  it("fetchEvents should throw an error on fetch failure", async () => {
    // Mock a failed fetch response
    fetchStub.resolves(
      new Response(null, {
        status: 500,
      })
    );

    try {
      await fetchEvents();
      throw new Error("fetchEvents did not throw");
    } catch (error) {
      expect(error).to.be.an("error");
      expect(error.message).to.equal("HTTP error! status: 500");
    }
  });

  it("fetchUserImage should return user image data on successful fetch", async () => {
    const username = "testuser";
    const mockUserData = ["imageId"];
    const mockImageData = { image: "base64ImageData" };

    fetchStub
      .withArgs(`http://localhost:8080/images/image/${username}`)
      .resolves(
        new Response(JSON.stringify(mockUserData), {
          status: 200,
          headers: { "Content-Type": "application/json" },
        })
      );
    fetchStub
      .withArgs(`http://localhost:8080/images/${mockUserData[0]}`)
      .resolves(
        new Response(JSON.stringify(mockImageData), {
          status: 200,
          headers: { "Content-Type": "application/json" },
        })
      );

    const imageBase64 = await fetchUserImage(username);
    expect(imageBase64).to.equal(mockImageData.image);
  });

  it("fetchUserImage should throw an error on fetch failure", async () => {
    const username = "testuser";
    fetchStub
      .withArgs(`http://localhost:8080/images/image/${username}`)
      .resolves(
        new Response(null, {
          status: 500,
        })
      );

    try {
      await fetchUserImage(username);
      throw new Error("fetchUserImage did not throw");
    } catch (error) {
      expect(error).to.be.an("error");
      expect(error.message).to.equal("HTTP error! status: 500");
    }
  });

  it("fetchUserDetails should return user details on successful fetch", async () => {
    const username = "testuser";
    const mockUserDetails = { id: 1, name: "Test User" };

    fetchStub
      .withArgs(`http://localhost:8080/images/image/${username}`)
      .resolves(
        new Response(JSON.stringify(mockUserDetails), {
          status: 200,
          headers: { "Content-Type": "application/json" },
        })
      );

    const userDetails = await fetchUserDetails(username);
    expect(userDetails).to.deep.equal(mockUserDetails);
  });

  it("fetchUserDetails should throw an error on fetch failure", async () => {
    const username = "testuser";

    fetchStub
      .withArgs(`http://localhost:8080/images/image/${username}`)
      .resolves(
        new Response(null, {
          status: 500,
        })
      );

    try {
      await fetchUserDetails(username);
      throw new Error("fetchUserDetails did not throw");
    } catch (error) {
      expect(error).to.be.an("error");
      expect(error.message).to.equal("HTTP error! status: 500");
    }
  });

  it("fetchImageData should return image data on successful fetch", async () => {
    const imageId = "12345";
    const mockImageData = { image: "base64ImageData" };

    fetchStub.withArgs(`http://localhost:8080/images/${imageId}`).resolves(
      new Response(JSON.stringify(mockImageData), {
        status: 200,
        headers: { "Content-Type": "application/json" },
      })
    );

    const imageData = await fetchImageData(imageId);
    expect(imageData).to.deep.equal(mockImageData);
  });

  it("fetchImageData should throw an error on fetch failure", async () => {
    const imageId = "12345";

    fetchStub.withArgs(`http://localhost:8080/images/${imageId}`).resolves(
      new Response(null, {
        status: 500,
      })
    );

    try {
      await fetchImageData(imageId);
      throw new Error("fetchImageData did not throw");
    } catch (error) {
      expect(error).to.be.an("error");
      expect(error.message).to.equal("HTTP error! status: 500");
    }
  });

  it("fetchImageId should return image ID on successful fetch", async () => {
    const mockResponse = new Response(JSON.stringify(["image123"]), {
      status: 200,
      headers: { "Content-type": "application/json" },
    });

    fetchStub
      .withArgs("http://localhost:8080/images/image/username")
      .returns(Promise.resolve(mockResponse));

    const result = await fetchImageId("username");
    expect(result).to.equal("image123");
  });

  it("fetchImageId should throw error on fetch failure", async () => {
    fetchStub.returns(Promise.resolve(new Response(null, { status: 500 })));

    try {
      await fetchImageId("username");
      throw new Error("Expected error was not thrown");
    } catch (error) {
      expect(error.message).to.equal("HTTP error! status: 500");
    }
  });

  it("fetchAndSetImageBase64 should return base64 image string on successful fetch", async () => {
    const mockResponse = new Response(
      JSON.stringify({ image: "base64ImageData" }),
      {
        status: 200,
        headers: { "Content-type": "application/json" },
      }
    );

    fetchStub
      .withArgs("http://localhost:8080/images/imageId")
      .returns(Promise.resolve(mockResponse));

    const result = await fetchAndSetImageBase64("imageId");
    expect(result).to.equal("base64ImageData");
  });

  it("fetchAndSetImageBase64 should return null for non-JSON response", async () => {
    const mockResponse = new Response("non-json data", {
      status: 200,
      headers: { "Content-type": "text/plain" },
    });

    fetchStub
      .withArgs("http://localhost:8080/images/imageId")
      .returns(Promise.resolve(mockResponse));

    const result = await fetchAndSetImageBase64("imageId");
    expect(result).to.be.null;
  });

  it("fetchImageByUsername should return user data on successful fetch", async () => {
    const mockResponse = new Response(
      JSON.stringify({ userData: "someData" }),
      {
        status: 200,
        headers: { "Content-type": "application/json" },
      }
    );

    fetchStub
      .withArgs("http://localhost:8080/images/image/username")
      .returns(Promise.resolve(mockResponse));

    const result = await fetchImageByUsername("username");
    expect(result).to.deep.equal({ userData: "someData" });
  });

  it("fetchInterests should return interests on successful fetch", async () => {
    const mockResponse = new Response(
      JSON.stringify(["Interest1", "Interest2"]),
      {
        status: 200,
        headers: { "Content-type": "application/json" },
      }
    );

    fetchStub
      .withArgs("http://localhost:8080/api/interests")
      .returns(Promise.resolve(mockResponse));

    const result = await fetchInterests();
    expect(result).to.deep.equal(["Interest1", "Interest2"]);
  });

  it("fetchUserInterests should return user interests on successful fetch", async () => {
    const mockResponse = new Response(
      JSON.stringify(["Interest1", "Interest2"]),
      {
        status: 200,
        headers: { "Content-type": "application/json" },
      }
    );

    fetchStub
      .withArgs("http://localhost:8080/api/interests/username/username")
      .returns(Promise.resolve(mockResponse));

    const result = await fetchUserInterests("username");
    expect(result).to.deep.equal(["Interest1", "Interest2"]);
  });

  it("fetchImageId should throw an error on fetch failure", async () => {
    fetchStub.returns(Promise.resolve(new Response(null, { status: 500 })));

    try {
      await fetchImageId("username");
      throw new Error("Expected error was not thrown");
    } catch (error) {
      expect(error.message).to.equal("HTTP error! status: 500");
    }
  });

  it("fetchAndSetImageBase64 should handle error on fetch failure", async () => {
    fetchStub.returns(Promise.resolve(new Response(null, { status: 500 })));

    const result = await fetchAndSetImageBase64("imageId");
    expect(result).to.be.null;
  });

  it("fetchAndSetImageBase64 should handle non-JSON response", async () => {
    const mockResponse = new Response("non-json data", {
      status: 200,
      headers: { "Content-type": "text/plain" },
    });

    fetchStub
      .withArgs("http://localhost:8080/images/imageId")
      .returns(Promise.resolve(mockResponse));

    const result = await fetchAndSetImageBase64("imageId");
    expect(result).to.be.null;
  });

  it("fetchImageByUsername should throw an error on fetch failure", async () => {
    fetchStub.returns(Promise.resolve(new Response(null, { status: 500 })));

    try {
      await fetchImageByUsername("username");
      throw new Error("Expected error was not thrown");
    } catch (error) {
      expect(error.message).to.equal("HTTP error! status: 500");
    }
  });

  it("fetchInterests should throw an error on fetch failure", async () => {
    fetchStub.returns(Promise.resolve(new Response(null, { status: 500 })));

    try {
      await fetchInterests();
      throw new Error("Expected error was not thrown");
    } catch (error) {
      expect(error.message).to.equal(
        "Error fetching interests. HTTP status: 500"
      );
    }
  });

  it("fetchUserInterests should throw an error on fetch failure", async () => {
    fetchStub.returns(Promise.resolve(new Response(null, { status: 500 })));

    try {
      await fetchUserInterests("username");
      throw new Error("Expected error was not thrown");
    } catch (error) {
      expect(error.message).to.equal(
        "Error fetching user interests. HTTP status: 500"
      );
    }
  });
});
