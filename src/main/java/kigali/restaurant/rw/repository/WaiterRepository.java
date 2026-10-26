package kigali.restaurant.rw.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.restaurant.rw.domain.Waiter;

@Repository 
public interface WaiterRepository extends JpaRepository<Waiter, UUID>{
    
    //used to block deleting a dining table that a waiter is still assigned to
    boolean existsByDiningTableId(UUID diningTableId);
}