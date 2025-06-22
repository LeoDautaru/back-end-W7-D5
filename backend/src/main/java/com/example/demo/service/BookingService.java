package com.example.demo.service;

import com.example.demo.model.Booking;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EventService eventService;

    public BookingService(BookingRepository bookingRepository, EventService eventService) {
        this.bookingRepository = bookingRepository;
        this.eventService = eventService;
    }

    // Prenota un posto per un utente su un evento
    public Optional<Booking> bookSeat(User user, Event event) {
        // Controlla disponibilità posti
        long bookedSeats = bookingRepository.countByEvent(event);
        if (bookedSeats >= event.getSeatsAvailable()) {
            return Optional.empty(); // posti esauriti
        }

        // Controlla se l'utente ha già prenotato
        if (bookingRepository.findByUserAndEvent(user, event).isPresent()) {
            return Optional.empty(); // già prenotato
        }

        Booking booking = new Booking(user, event);
        return Optional.of(bookingRepository.save(booking));
    }

    // Annulla prenotazione
    public boolean cancelBooking(User user, Event event) {
        Optional<Booking> bookingOpt = bookingRepository.findByUserAndEvent(user, event);
        if (bookingOpt.isPresent()) {
            bookingRepository.delete(bookingOpt.get());
            return true;
        }
        return false;
    }

    // Lista prenotazioni di un utente
    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }
}
