package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "event_id"})})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id")
    private Event event;

    public Booking() {}

    public Booking(User user, Event event) {
        this.user = user;
        this.event = event;
    }

    // getters e setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
}
