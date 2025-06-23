import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Navbar from "./components/Navbar";
import Login from "./components/Login";
import Register from "./components/Register";
import EventList from "./components/EventsList";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

function App() {
  const [user, setUser] = useState(null);

  const handleLogin = (userData) => {
    setUser(userData);
  };

  const handleLogout = () => {
    setUser(null);
    // qui puoi anche pulire token o altro storage
  };

  return (
    <Router>
      <Navbar user={user} onLogout={handleLogout} />
      <Routes>
        <Route path="/login" element={<Login onLogin={handleLogin} />} />
        <Route path="/register" element={<Register />} />
        <Route path="/" element={<EventList />} />
      </Routes>
    </Router>
  );
}

export default App;
