package kigali.restaurant.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.restaurant.rw.domain.Waiter;

@Repository
public interface WaiterRepository extends JpaRepository<Waiter, UUID>{

    boolean existsByDiningTableId(UUID diningTableId);

    // C1
    @Query("SELECT DISTINCT w FROM Waiter w JOIN w.shifts s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY w.lastName ASC, w.firstName ASC")
    List<Waiter> findByShiftName(@Param("name") String name);

    // D4
    boolean existsByDiningTableTableNumberAndIdNot(int tableNumber,  UUID waiterId);
}