package kigali.restaurant.rw.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import kigali.restaurant.rw.domain.Waiter;

@Repository 
public interface WaiterRepository extends JpaRepository<Waiter, UUID>{
    
    boolean existsByDiningTableId(UUID diningTableId);

    //C1: waiters who hold a shift, shift name matched without regard to letter case
    @Query("SELECT DISTINCT w FROM Waiter w JOIN w.shifts s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY w.lastName ASC, w.firstName ASC")
    List<Waiter> findByShiftName(@Param("name") String name);

    //D4: another waiter (not this one) already occupies that dining table
    boolean existsByDiningTableTableNumberAndIdNot(int tableNumber, UUID waiterId);
}