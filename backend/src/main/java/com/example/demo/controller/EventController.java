package com.example.demo.controller;

import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.service.EventService;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    // GET /api/events — lista tutti gli eventi
    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // GET /api/events/{id} — dettagli evento
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> eventOpt = eventService.getEventById(id);
        return eventOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /api/events — crea evento (solo organizzatore)
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Event event, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Utente non autenticato");
        }

        User user = userOpt.get();
        if (!user.getRoles().contains("ROLE_ORGANIZER")) {
            return ResponseEntity.status(403).body("Permesso negato: non sei un organizzatore");
        }

        event.setCreator(user);  // assegna creatore
        Event savedEvent = eventService.createEvent(event);
        return ResponseEntity.ok(savedEvent);
    }

    // PUT /api/events/{id} — modifica evento (solo creatore)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Utente non autenticato");
        }
        User user = userOpt.get();

        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Event event = eventOpt.get();

        if (!event.getCreator().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Permesso negato: non sei il creatore dell'evento");
        }

        // aggiorna i campi (esempio)
        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setDate(updatedEvent.getDate());
        event.setLocation(updatedEvent.getLocation());
        event.setSeatsAvailable(updatedEvent.getSeatsAvailable());

        Event saved = eventService.updateEvent(event);
        return ResponseEntity.ok(saved);
    }

    // DELETE /api/events/{id} — elimina evento (solo creatore)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Utente non autenticato");
        }
        User user = userOpt.get();

        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Event event = eventOpt.get();

        if (!event.getCreator().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Permesso negato: non sei il creatore dell'evento");
        }

        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
