package kigali.restaurant.rw.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import kigali.restaurant.rw.domain.Shift;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    
    //C3: shifts that no waiter holds
    @Query("SELECT s FROM Shift s WHERE s.waiters IS EMPTY ORDER BY s.name ASC")
    List<Shift> findUnusedShifts();
}
