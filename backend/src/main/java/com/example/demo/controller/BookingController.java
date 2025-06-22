package com.example.demo.controller;

import com.example.demo.model.Booking;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.service.BookingService;
import com.example.demo.service.EventService;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final EventService eventService;

    public BookingController(BookingService bookingService, UserService userService, EventService eventService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.eventService = eventService;
    }

    // POST /api/bookings/{eventId} - Prenota un posto per l'evento
    @PostMapping("/{eventId}")
    public ResponseEntity<?> bookEvent(@PathVariable Long eventId, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Utente non autenticato");
        }
        User user = userOpt.get();

        Optional<Event> eventOpt = eventService.getEventById(eventId);
        if (eventOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Event event = eventOpt.get();

        Optional<Booking> bookingOpt = bookingService.bookSeat(user, event);
        if (bookingOpt.isEmpty()) {
            return ResponseEntity.status(400).body("Prenotazione non possibile: posti esauriti o già prenotato");
        }

        return ResponseEntity.ok(bookingOpt.get());
    }

    // GET /api/bookings - Visualizza le prenotazioni dell'utente
    @GetMapping
    public ResponseEntity<?> getUserBookings(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Utente non autenticato");
        }
        User user = userOpt.get();

        List<Booking> bookings = bookingService.getBookingsByUser(user);
        return ResponseEntity.ok(bookings);
    }

    // DELETE /api/bookings/{eventId} - Annulla prenotazione per evento
    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long eventId, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Utente non autenticato");
        }
        User user = userOpt.get();

        Optional<Event> eventOpt = eventService.getEventById(eventId);
        if (eventOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Event event = eventOpt.get();

        boolean canceled = bookingService.cancelBooking(user, event);
        if (!canceled) {
            return ResponseEntity.status(400).body("Prenotazione non trovata");
        }
        return ResponseEntity.noContent().build();
    }
}
