import React from "react";
import EventBoard from "./components/eventboard/EventBoard";
import Sidebar from "./components/sidebar/Sidebar";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import Account from "./components/Account/Account";
import EventDetails from "./components/event_card/EventDetails";
import CreateEvent from "./components/create_event/CreateEvent";
import Login from "./components/login/Login";
import "./App.css";
import Footer from "./components/footer/Footer";

function App() {
  return (
    <>
      <Router>
        <div className="divide-wall"></div>
        <div className="App">
          <Routes>
            <Route path="/login" element={<Login />} />
          </Routes>
          <Sidebar />
          <Routes>
            <Route path="/" element={<EventBoard />} />
            <Route path="/account" element={<Account />} />
            <Route path="/login/register" element={<Login />} />
            <Route path="/event-details" element={<EventDetails />} />
            <Route path="/create-events" element={<CreateEvent />} />
          </Routes>
          <p className="component-heading">
            <p className="component-text-1">U-</p>{" "}
            <p className="component-text-2">Event</p>
          </p>
        </div>
        <Footer />
      </Router>
    </>
  );
}

export default App;
