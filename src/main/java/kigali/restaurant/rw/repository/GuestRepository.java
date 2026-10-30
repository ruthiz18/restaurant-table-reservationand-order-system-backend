package kigali.restaurant.rw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
  
import kigali.restaurant.rw.domain.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest,  UUID> {

    Optional<Guest> findByPhone(String phone);
  
    // B1
    List<Guest> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);
  
    // C4
    @Query("SELECT DISTINCT g FROM Reservation r JOIN r.guest g WHERE r.waiter.id = :waiterId ORDER BY g.lastName ASC, g.firstName ASC")
    List<Guest> findGuestsOfWaiter(@Param("waiterId") UUID waiterId);
}
