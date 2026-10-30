package kigali.restaurant.rw.repository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.restaurant.rw.domain.Reservation;
import kigali.restaurant.rw.domain.ReservationStatus;

@Repository 
public interface ReservationRepository extends JpaRepository<Reservation,  UUID>{

    boolean existsByGuestId(UUID guestId);

    boolean existsByWaiterId(UUID waiterId);

    // B2
    List<Reservation> findByStatusOrderByReservationDateAsc(ReservationStatus status);

    // B3
    List<Reservation> findByReservationDateBetweenOrderByReservationDateAsc(LocalDate start,  LocalDate end);

    // D1
    boolean existsByWaiterIdAndReservationDateAndStatusNot(UUID waiterId, LocalDate reservationDate, ReservationStatus status);

    // F1
    @Query("SELECT r.status, COUNT(r) FROM Reservation r GROUP BY r.status ORDER BY COUNT(r) DESC, r.status ASC")
    List<Object[]> countByStatus();

    // F2
    @Modifying
    @Transactional
    @Query("""
            UPDATE Reservation r SET r.status = :withdrawn
            WHERE r.waiter.id = :waiterId AND r.reservationDate = :date
              AND r.status NOT IN (:seated, :withdrawn)
            """)
    int cancelDay(@Param("waiterId") UUID waiterId, @Param("date") LocalDate date,
                  @Param("seated") ReservationStatus seated, @Param("withdrawn") ReservationStatus withdrawn);

    // Dashboard
    long countByReservationDate(LocalDate reservationDate);

    long countByStatusEquals(ReservationStatus status);
}