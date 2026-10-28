package kigali.restaurant.rw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.restaurant.rw.domain.Guest;

@Repository 
public interface GuestRepository extends JpaRepository<Guest, UUID> {

    Optional<Guest> findByPhone(String phone);

    //B1: same last name whatever the letter case, first name A to Z
    List<Guest> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    //C4: guests with at least one reservation with this waiter, each listed once
    @Query("SELECT DISTINCT g FROM Reservation r JOIN r.guest g WHERE r.waiter.id = :waiterId ORDER BY g.lastName ASC, g.firstName ASC")
    List<Guest> findGuestsOfWaiter(@Param("waiterId") UUID waiterId);
}
