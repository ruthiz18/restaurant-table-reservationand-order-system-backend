package kigali.restaurant.rw.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.restaurant.rw.domain.Reservation;

@Repository 
public interface ReservationRepository extends JpaRepository<Reservation, UUID>{

    boolean existsByGuestId(UUID guestId);

    boolean existsByWaiterId(UUID waiterId);

}
