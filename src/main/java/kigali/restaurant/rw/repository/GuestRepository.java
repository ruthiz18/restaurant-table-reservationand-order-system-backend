package kigali.restaurant.rw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kigali.restaurant.rw.domain.Guest;

@Repository 
public interface GuestRepository extends JpaRepository<Guest, UUID> {

    Optional<Guest> findByPhone(String phone);

    //B1: same last name whatever the letter case, first name A to Z
    List<Guest> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);
}
