import React, { useState, useEffect } from "react";
import EventCardForm from "./EventCardForm.jsx";
import "../../css/EventCardForm.css";
import { handleVerification } from "../../handlers/handler";

const ManageEvents = () => {
  // Verification
  handleVerification();

  const [events, setEvents] = useState([]);
  const [editingEventId, setEditingEventId] = useState(null);
  const [editingEventData, setEditingEventData] = useState(null);
  const username = localStorage.getItem("username");

  // updated 12-3-23 - Shows only events created by the user logged in in manage events section
  useEffect(() => {
    fetch("http://localhost:8080/api/events/")
      .then((res) => {
        if (!res.ok) {
          throw new Error("Failed to fetch events.");
        }
        return res.json();
      })
      .then((data) => {
        console.log(data);
        // filter events by user and show only events created by the user logged in
        data = data.filter((event) => event.user.username === username);

        setEvents(data);
      })
      .catch((error) => {
        console.error("Error fetching events:", error);
      });
  }, []);

  const deleteEvent = (id) => {
    fetch(`http://localhost:8080/api/events/${id}`, {
      method: "DELETE",
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Failed to delete event.");
        }
        setEvents((prevEvents) =>
          prevEvents.filter((event) => event.eventId !== id)
        );
      })
      .catch((error) => {
        console.error("Error deleting event:", error);
      });
  };

  const startEditingEvent = (event) => {
    setEditingEventId(event.eventId);
    setEditingEventData(event);
  };

  const handleEditChange = (name, value) => {
    setEditingEventData((previousData) => {
      if (name.startsWith("user.")) {
        const userFieldName = name.split(".")[1];
        return {
          ...previousData,
          user: {
            ...previousData.user,
            [userFieldName]: value,
          },
        };
      }
      return {
        ...previousData,
        [name]: value,
      };
    });
  };

  const handleEditSubmit = (id) => {
    fetch(`http://localhost:8080/api/events/update/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(editingEventData),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Failed to update event.");
        }
        return res.json();
      })
      .then((updatedEvent) => {
        setEvents((previousEvents) =>
          previousEvents.map((event) =>
            event.eventId === id ? updatedEvent : event
          )
        );
        setEditingEventId(null);
        setEditingEventData(null);
      })
      .catch((error) => {
        console.error("Error updating event:", error);
      });
  };

  const [isEdit, setIsEdit] = useState(false);

  return (
    <div className="manage-events-container">
      <div className="manage-events-main-container">
        <div className="manage-events-header">
          <h1>Manage Events</h1>
        </div>
        <div className="manage-events-body">
          {events.map((event) => (
            <div key={event.eventId}>
              {editingEventId === event.eventId ? (
                <EventCardForm
                  {...editingEventData}
                  onSubmit={() => handleEditSubmit(event.eventId)}
                  onChange={handleEditChange}
                  width={380}
                  isEdit={isEdit}
                />
              ) : (
                <>
                  <EventCardForm
                    {...event}
                    width={400}
                    username={event.user.username}
                  />
                  <button onClick={() => deleteEvent(event.eventId)}>
                    Delete
                  </button>
                  <button
                    onClick={() => {
                      setIsEdit(true);
                      startEditingEvent(event);
                    }}
                  >
                    Edit
                  </button>
                </>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ManageEvents;
