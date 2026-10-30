package kigali.restaurant.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.restaurant.rw.domain.Reservation;
import kigali.restaurant.rw.domain.TableOrder;

public interface TableOrderRepository extends JpaRepository<TableOrder,  UUID>{

    // E3
    boolean existsByReservation(Reservation reservation);

    // E2
    @Query("SELECT o FROM TableOrder o WHERE o.reservation.guest.phone = :phone ORDER BY o.orderDate DESC, o.dishName ASC")
    List<TableOrder> findByGuestPhone(@Param("phone") String phone);
}
