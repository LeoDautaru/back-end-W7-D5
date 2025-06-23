import React, { useEffect, useState } from "react";

function EventList() {
  const [events, setEvents] = useState([]);

  useEffect(() => {
    fetch("/api/events")
      .then((res) => res.json())
      .then((data) => setEvents(data))
      .catch(console.error);
  }, []);

  return (
    <div className="container mt-4">
      <h2>Eventi</h2>
      {events.length === 0 ? (
        <p>Nessun evento disponibile.</p>
      ) : (
        <ul className="list-group">
          {events.map((event) => (
            <li key={event.id} className="list-group-item">
              <h5>{event.title}</h5>
              <p>{event.description}</p>
              <small>{new Date(event.date).toLocaleString()}</small>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default EventList;
