package kigali.restaurant.rw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kigali.restaurant.rw.domain.DiningTable;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTable, UUID> {

    // C2
    @Query("SELECT t FROM DiningTable t WHERE NOT EXISTS (SELECT w.id FROM Waiter w WHERE w.diningTable = t) ORDER BY t.tableNumber ASC")
    List<DiningTable> findFreeDiningTables();

    // D4
    Optional<DiningTable> findByTableNumber(int tableNumber);
}
