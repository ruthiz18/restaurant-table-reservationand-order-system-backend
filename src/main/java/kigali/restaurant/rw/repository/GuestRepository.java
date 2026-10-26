package kigali.restaurant.rw.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.restaurant.rw.domain.Guest;

@Repository 
public interface GuestRepository extends JpaRepository<Guest, UUID> {

    //phone is the business key of a guest
    Optional<Guest> findByPhone(String phone);
}
