package com.example.demo.repository;

import com.example.demo.model.Booking;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Trova tutte le prenotazioni fatte da un utente
    List<Booking> findByUser(User user);

    // Controlla se un utente ha già prenotato un evento
    Optional<Booking> findByUserAndEvent(User user, Event event);

    // Conta quante prenotazioni ha un evento
    long countByEvent(Event event);
}
