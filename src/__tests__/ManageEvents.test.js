import React from 'react';
import { expect } from 'chai';
import { render, cleanup, act, waitFor } from '@testing-library/react';
import sinon from 'sinon';
import ManageEvents from '../components/mainui/ManageEvents';

describe('ManageEvents Component', () => {
  let stub;

  // Set up a stub for fetch
  beforeEach(() => {
    stub = sinon.stub(global, 'fetch');
    stub.resolves({
      json: () => Promise.resolve([]), // Assuming it resolves an empty array initially
      ok: true,
    });
  });

  // Clean up after each test
  afterEach(() => {
    cleanup();
    stub.restore();
  });

  it('renders without crashing', async () => {
    const { getByText, container } = render(<ManageEvents />);
    console.log(container.innerHTML);

    await act(async () => {
      const { getByText } = render(<ManageEvents />);
      await waitFor(() => {
        expect(getByText(/Manage Events/i)).to.exist;
      });
    });
  });

});
