package kigali.restaurant.rw.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.restaurant.rw.domain.DiningTable;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTable, UUID> {
    
}
