package kigali.restaurant.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.restaurant.rw.domain.Guest;
import kigali.restaurant.rw.repository.GuestRepository;
import kigali.restaurant.rw.repository.ReservationRepository;

@Service 
public class GuestService{

    public static final String SAVED = "Guest saved successfully";
    public static final String UPDATED = "Guest updated successfully";
    public static final String DELETED = "Guest deleted successfully";
    public static final String NOT_FOUND = "The guest with that id does not exist";
    public static final String FIELDS_REQUIRED = "firstname, last name and phone are required";
    public static final String PHONE_TAKEN = "A guest with that phone already exists";
    public static final String IN_USE = "The guest has reservations, delete them first";

    @Autowired 
    private GuestRepository guestRepo;

    @Autowired 
    private ReservationRepository reservationRepo;

    private boolean missingFields(Guest guest){
        return guest.getFirstName() == null || guest.getFirstName().isBlank() || guest.getLastName() == null || guest.getLastName().isBlank() || guest.getPhone() == null || guest.getPhone().isBlank();
    }

    public String saveGuest(Guest guest){
        if(missingFields(guest)){
            return FIELDS_REQUIRED;
        }
        guest.setPhone(guest.getPhone().trim());
        if(guestRepo.findByPhone(guest.getPhone()).isPresent()){
            return PHONE_TAKEN;
        }
        guestRepo.save(guest);
        return SAVED;
    }

    public List<Guest> getAllGuest(){
        return guestRepo.findAll();
    }

    public Guest getGuestById(UUID id){
        return guestRepo.findById(id).orElse(null);
    }

    public String updateGuest(UUID id, Guest updatedGuest){
        Optional<Guest> getOne = guestRepo.findById(id);
        if(getOne.isEmpty()){
            return NOT_FOUND;
        }
        if(missingFields(updatedGuest)){
            return FIELDS_REQUIRED;
        }
        String phone = updatedGuest.getPhone().trim();
        Optional<Guest> samePhone = guestRepo.findByPhone(phone);
        if(samePhone.isPresent() && !samePhone.get().getId().equals(id)){
            return PHONE_TAKEN;
        }
        Guest existing = getOne.get();
        existing.setFirstName(updatedGuest.getFirstName());
        existing.setLastName(updatedGuest.getLastName());
        existing.setPhone(phone);
        existing.setEmail(updatedGuest.getEmail());
        return UPDATED;
    }

    public String deleteGuest(UUID id){
        if(!guestRepo.existsById(id)){
            return NOT_FOUND;
        }
        if(reservationRepo.existsByGuestId(id)){
            return IN_USE;
        }
        guestRepo.deleteById(id);
        return DELETED;

    }
}