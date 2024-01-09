import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
// CSS
import "./EventBoard.css";
import "../../components/event_card/EventCard.css";
// APIs and Handlers
import { fetchEvents } from "../../handlers/api";
import {
  handleFilterChange,
  handleSearchChange,
  handleVerification,
} from "../../handlers/handler";
// Components
import EventCard from "../event_card/EventCard";

const EventBoard = () => {
  handleVerification();
  const [data, setData] = useState([]);
  const [filter, setFilter] = useState("all");
  const [filteredData, setFilteredData] = useState([]);
  const [searchInput, setSearchInput] = useState("");

  useEffect(() => {
    fetchEvents().then((data) => {
      setData(data);
    });
  }, []);

  useEffect(() => {
    setFilteredData(filterData(filter, data));
  }, [filter, data]);

  const filterData = (filter) => {
    return data.filter((event) => event.category === filter);
  };

  return (
    <section className="event-board" data-testid="event-board">
      <p className="welcome-text">{`Welcome back ${
        localStorage.getItem("firstName")
          ? localStorage.getItem("firstName")
          : "User"
      }!`}</p>

      <div className="event-board-top">
        <div className="event-board-header">
          <div className="event-board-filter">
            <h1 className="event-board-title">Event Board</h1>
            <p className="filter-text">Filter By</p>
            <select
              className="event-board-filter"
              data-testid="event-board-filter"
              onChange={(e) => handleFilterChange(e, setFilter)}
            >
              <option value="all">All</option>
              <option value="concert">Concert</option>
              <option value="sports">Sports</option>
              <option value="food">Food</option>
              <option value="outdoors">Outdoors</option>
              <option value="games">Games</option>
              <option value="other">Other</option>
            </select>
          </div>
          <div className="search-container">
            <input
              className="search-bar"
              data-testid="search-bar"
              type="text"
              placeholder="Search for an event..."
              onChange={(e) =>
                handleSearchChange(e, data, setSearchInput, setFilteredData)
              }
            />
          </div>
        </div>
      </div>

      <div className="event-board-top">
        <div className="event-board-events">
          {searchInput.length > 0 || filter !== "all"
            ? filteredData.map((event) => (
                <Link
                  to="/event-details"
                  state={{ event }}
                  key={event.eventId}
                  className="event-board-event"
                >
                  <div className="zoom-effect">
                    <EventCard {...event} height={600} />
                  </div>
                </Link>
              ))
            : data.map((event) => (
                <Link
                  to="/event-details"
                  state={{ event }}
                  key={event.eventId}
                  className="event-board-event"
                >
                  <div className="zoom-effect">
                    <EventCard
                      {...event}
                      eventCreator={event.user}
                      height={600}
                    />
                  </div>
                </Link>
              ))}
        </div>
      </div>
    </section>
  );
};

export default EventBoard;
