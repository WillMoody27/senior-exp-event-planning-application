// API Requests ---------------------------------------------------------------

export const fetchEvents = async () => {
  try {
    const response = await fetch("http://localhost:8080/api/events");
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    throw error;
  }
};

export const fetchUserImage = async (username) => {
  try {
    const responseUserData = await fetch(
      `http://localhost:8080/images/image/${username}`
    );
    const userData = await responseUserData.json();
    const imageId = userData[0]; // Assuming userData is an array

    const responseImage = await fetch(
      `http://localhost:8080/images/${imageId}`
    );
    const imageData = await responseImage.json();

    return imageData.image;
  } catch (error) {
    throw error;
  }
};

// EventDetails API Requests ---------------------------------------------------
// Add these functions in your api.js

export const fetchUserDetails = async (username) => {
  try {
    const response = await fetch(
      `http://localhost:8080/images/image/${username}`
    );
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    throw error;
  }
};

export const fetchImageData = async (imageId) => {
  try {
    const response = await fetch(`http://localhost:8080/images/${imageId}`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    throw error;
  }
};

// API Call to fetch imageId from username
export const fetchImageId = async (username) => {
  try {
    const response = await fetch(
      `http://localhost:8080/images/image/${username}`
    );
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();

    if (!data || !data[0]) {
      throw new Error("Image ID not found in the response.");
    }

    return data[0];
  } catch (error) {
    console.error("Error fetching image:", error);
    return null;
  }
};

// API Call to fetch imageId and setbase64Image
export const fetchAndSetImageBase64 = async (imageId) => {
  try {
    const response = await fetch(`http://localhost:8080/images/${imageId}`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const contentType = response.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
      // Check if the response contains JSON data
      const data = await response.json();
      if (data.image) {
        return data.image;
      } else {
        console.error("Image data is missing in the response.");
        return null;
      }
    } else {
      // If the response is not JSON, assume it's a base64 image string
      return await response.text();
    }
  } catch (error) {
    console.error("Error fetching image:", error);
    return null;
  }
};

// EventDetails API Requests ---------------------------------------------------
export const fetchImageByUsername = async (username) => {
  try {
    const response = await fetch(
      `http://localhost:8080/images/image/${username}`
    );
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const userData = await response.json();
    return userData;
  } catch (error) {
    console.error("Error fetching image:", error);
    return null;
  }
};

export const fetchInterests = async () => {
  try {
    const response = await fetch(`http://localhost:8080/api/interests`);
    if (!response.ok) {
      throw new Error(
        `Error fetching interests. HTTP status: ${response.status}`
      );
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching interests:", error.message);
    return null;
  }
};

// API call to fetch all the interests of a user
export const fetchUserInterests = async (username) => {
  try {
    const response = await fetch(
      `http://localhost:8080/api/interests/username/${username}`
    );
    if (!response.ok) {
      throw new Error(
        `Error fetching user interests. HTTP status: ${response.status}`
      );
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching user interests:", error.message);
    return null;
  }
};
