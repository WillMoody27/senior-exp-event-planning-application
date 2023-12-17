import React from 'react';
import { render, fireEvent, waitFor, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import Account from '../components/Account/Account';
import { MemoryRouter, useNavigate } from 'react-router-dom';
import { expect } from 'chai';
import sinon from 'sinon';


describe('<Account /> Component', () => {
  let fetchStub;
  const username = "testuser"; // Define a username for test purposes

  beforeEach(() => {
    fetchStub = sinon.stub(global, 'fetch').resolves({
      ok: true,
      json: () => Promise.resolve({ /* mock response data */ })
    });

    localStorage.setItem("username", username);
  });

  afterEach(() => {
    fetchStub.restore();
    localStorage.removeItem("username");
  });

  const renderComponent = () =>
    render(
        <MemoryRouter>
            <Account />
        </MemoryRouter>
    );

  it('renders without crashing', () => {
    const { getByText } = renderComponent();
    expect(getByText(/logout/i)).to.exist;
  });

  it('contains a logout button', () => {
    const { getByText } = renderComponent();
    const logoutButton = getByText('Logout');
    expect(logoutButton).to.exist;
  });

  // Mocking an API call
  // it('makes an API call to fetch user data on mount', async () => {
  //   renderComponent();
  //   await waitFor(() => expect(fetchStub.calledOnce).to.be.true);
  // });

  // Testing form submission
  it('submits the form with user details', async () => {
    const { getByText, getByLabelText } = renderComponent();
    const submitButton = getByText('Update');

    fireEvent.change(getByLabelText('First Name'), { target: { value: 'John' } });
    fireEvent.click(submitButton);

    // Check if the first name was updated in the form
    await waitFor(() => {
        const firstNameInput = getByLabelText('First Name');
        expect(firstNameInput.value).to.equal('John');
    });
  });

  it('initially shows a placeholder image', () => {
    const { getByAltText } = renderComponent();
    const placeholderImage = getByAltText('image_alt');
    expect(placeholderImage.getAttribute('src')).to.equal('https://via.placeholder.com/150');
  });

  // Interaction test
  it('calls handleUpload when upload image button is clicked', async () => {
    const { getByText, container } = renderComponent();
    const uploadButton = getByText('Upload Image');

    fireEvent.click(uploadButton);

    // Check for some change that handleUpload causes, for example, a loading state, a console log, etc.
    // As an example, checking if the fetchStub was called as part of handleUpload
    await waitFor(() => expect(fetchStub.called).to.be.true);
  });
  
  it('uploads an image when a file is selected', async () => {
    const { container, getByText } = renderComponent();
    const fileInput = container.querySelector('input[type="file"]');
    const testFile = new File(['(⌐□_□)'], 'chucknorris.png', { type: 'image/png' });
  
    fireEvent.change(fileInput, { target: { files: [testFile] } });
  
    const uploadButton = getByText('Upload Image');
    fireEvent.click(uploadButton);
  
    // Check if the fetchStub was called with the correct arguments
    await waitFor(() => expect(fetchStub.calledWithMatch(`http://localhost:8080/images/uploadImage/`)).to.be.true);
  });

  it('displays the uploaded image', async () => {
    const { getByAltText } = renderComponent();
  
    // Mock fetching an image after upload
    fetchStub.resolves({
      ok: true,
      json: () => Promise.resolve({ image: 'base64ImageString' })
    });
  
    // Simulate the effect after image upload
    await waitFor(() => {
      const uploadedImage = getByAltText('image_alt');
      expect(uploadedImage.src).contain('base64ImageString');
    });
  });
  
  it('deletes the image when delete icon is clicked', async () => {
    const { getByTestId, getByAltText } = renderComponent(); // use getByTestId and getByAltText
    const deleteIcon = getByTestId('delete-icon');
  
    fireEvent.click(deleteIcon);
  
    // Check if the fetchStub was called for deletion
    await waitFor(() => expect(fetchStub.calledWithMatch(`http://localhost:8080/images/deleteImage/`)).to.be.true);
  
    // Check if the placeholder image is displayed after the delete action
    const placeholderImage = getByAltText('image_alt');
    expect(placeholderImage.src).equal('https://via.placeholder.com/150');
  });

  it('handles network error on fetching image gracefully', async () => {
    fetchStub.withArgs(`http://localhost:8080/images/image/${username}`).rejects(new Error("Network Error"));

    const { getByAltText } = renderComponent();

    await waitFor(() => {
      const image = getByAltText('image_alt');
      expect(image.src).equal('https://via.placeholder.com/150'); // Assuming a placeholder is shown on error
    });
  });

  // it('opens file input when upload button is clicked', () => {
  //   const { getByText, container } = renderComponent(); // include container here
  //   const uploadButton = getByText('Upload Image');
  //   const fileInput = container.querySelector('input[type="file"]');
  
  //   fireEvent.click(uploadButton);
  
  //   // Check if the file input is in focus or simulated a click
  //   expect(document.activeElement).equal(fileInput);
  // });
  
  // it('clears local storage and navigates to login on logout', () => {
  //   const { getByText } = renderComponent();

  //   // Assuming the navigate function is mocked to check if it has been called with the correct argument
  //   const mockNavigate = jest.fn();
  //   useNavigate.mockReturnValue(mockNavigate);

  //   const logoutButton = getByText('Logout');
  //   fireEvent.click(logoutButton);

  //   // Check if local storage is cleared
  //   expect(localStorage.clear).toHaveBeenCalledTimes(1);

  //   // Check if navigate was called with '/login'
  //   expect(mockNavigate).toHaveBeenCalledWith('/login');
  // });

  
});

// it('shows error message on failed image upload', async () => {
  //   const { getByText, container } = renderComponent();
  //   const uploadButton = getByText('Upload Image');
  //   const fileInput = container.querySelector('input[type="file"]');
  //   const testFile = new File(['test'], 'test.png', { type: 'image/png' });
  
  //   fireEvent.change(fileInput, { target: { files: [testFile] } });
  //   fetchStub.withArgs(`http://localhost:8080/images/uploadImage/${username}`).rejects(new Error("Upload Failed"));
    
  //   fireEvent.click(uploadButton);
    
  //   await waitFor(() => {
  //     expect(getByText("Error uploading image:")).toBeInTheDocument(); // Assuming an error message is displayed
  //   });
  // });
  
  // it('handles error on fetching user data', async () => {
  //   fetchStub.withArgs(`http://localhost:8080/api/users/${username}`).rejects(new Error("Failed to fetch user data"));
  
  //   const { getByText } = renderComponent();
    
  //   await waitFor(() => {
  //     expect(getByText("Error fetching user data")).toBeInTheDocument(); // Assuming an error message is shown
  //   });
  // });

  // it('shows error on failed user details update', async () => {
  //   const { getByText, getByLabelText } = renderComponent();
  //   const updateButton = getByText('Update');
  
  //   fireEvent.change(getByLabelText('First Name'), { target: { value: 'John' } });
  //   fetchStub.withArgs(`http://localhost:8080/api/users/${localStorage.getItem("username")}`).rejects(new Error("Update Failed"));
  
  //   fireEvent.click(updateButton);
    
  //   await waitFor(() => {
  //     expect(getByText("User Details Not Updated")).toBeInTheDocument(); // Assuming an error message is shown on update failure
  //   });
  // });