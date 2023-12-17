// Handler Functions -----------------------------------------------------------

export const handleFilterChange = (e, setFilter) => {
  const filter = e.target.value;
  setFilter(filter);
};

export const handleSearchChange = (
  e,
  data,
  setSearchInput,
  setFilteredData
) => {
  const searchValue = e.target.value.toLowerCase();
  setSearchInput(searchValue);

  const updatedFilteredData = data.filter((event) => {
    return event.eventName.toLowerCase().includes(searchValue);
  });

  setFilteredData(updatedFilteredData);
};

export const filterData = (filter, data) => {
  return data.filter((event) => event.category === filter);
};

// This export handles the user's login credentials
export const handleVerification = () => {
  // redirect user to account page if not logged in
  if (!localStorage.getItem("token") && !localStorage.getItem("username")) {
    window.location.href = "/login";
  }
};






// Fetching User Image ---------------------------------------------------------

// Fetching User Image ---------------------------------------------------------
