package kigali.restaurant.rw.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.restaurant.rw.domain.Reservation;
import kigali.restaurant.rw.domain.ReservationStatus;

@Repository 
public interface ReservationRepository extends JpaRepository<Reservation, UUID>{

    boolean existsByGuestId(UUID guestId);

    boolean existsByWaiterId(UUID waiterId);

    //B2: one status, earliest date first
    List<Reservation> findByStatusOrderByReservationDateAsc(ReservationStatus status);

    //B3: both dates inclusive, ordered by date
    List<Reservation> findByReservationDateBetweenOrderByReservationDateAsc(LocalDate start, LocalDate end);

}
