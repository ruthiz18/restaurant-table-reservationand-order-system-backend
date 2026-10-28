package kigali.restaurant.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.restaurant.rw.domain.Guest;
import kigali.restaurant.rw.domain.Reservation;
import kigali.restaurant.rw.domain.ReservationStatus;
import kigali.restaurant.rw.domain.Waiter;
import kigali.restaurant.rw.repository.GuestRepository;
import kigali.restaurant.rw.repository.ReservationRepository;
import kigali.restaurant.rw.repository.WaiterRepository;

@Service 
public class ReservationService {

    public static final String SAVED = "Reservation saved successfully";
    public static final String UPDATED = "Reservation updated successfully";
    public static final String DELETED = "Reservation deleted successfully";
    public static final String NOT_FOUND = "The reservation with that id does not exist";
    public static final String FIELDS_REQUIRED = "reservation date, time slot and a party size greater than 0 are required";
    public static final String GUEST_ID_REQUIRED = "guest id is required";
    public static final String WAITER_ID_REQUIRED = "waiter is required";
    public static final String PAST_DATE = "Reservation date cannot be in the past";
    public static final String WAITER_BOOKED = "Waiter is already booked on that date";
    public static final String STATUS_UPDATED = "Reservation status pdated successfully";
    public static final String CANNOT_CHANGE_PREFIX = "Cannot change status from ";
    public static final String INVALID_DATE = "start adn end date must be dates written as yyyy-mm-dd";
    public static final String START_AFTER_END = "start date cannot be after the end date";

     

    @Autowired
    private ReservationRepository reservationRepo;

    @Autowired 
    private GuestRepository guestRepo;

    @Autowired 
    private WaiterRepository waiterRepo;

    private boolean missingFields(Reservation reservation){
        return reservation.getReservationDate() == null || reservation.getTimeSlot() == null || reservation.getTimeSlot().isBlank() || reservation.getPartySize() <=0;
    }

    public String saveReservation(Reservation reservation, UUID guestId, UUID waiterId){
        if(missingFields(reservation)){
            return FIELDS_REQUIRED;
        }
        if(guestId == null){
            return GUEST_ID_REQUIRED;
        }
        if(waiterId == null){
            return WAITER_ID_REQUIRED;
        }
        Optional<Guest> getGuest = guestRepo.findById(guestId);
        if(getGuest.isEmpty()){
            return GuestService.NOT_FOUND;
        }
        Optional<Waiter> getWaiter = waiterRepo.findById(waiterId);
        if(getWaiter.isEmpty()){
            return WaiterService.NOT_FOUND;
        }
        reservation.setGuest(getGuest.get());
        reservation.setWaiter(getWaiter.get());
        if(reservation.getStatus() == null){
            reservation.setStatus(ReservationStatus.REQUESTED);
        }
        reservationRepo.save(reservation);
        return SAVED;
    }

    public List<Reservation> getAllReservations(){
        return reservationRepo.findAll();
    }

    public List<Reservation> getReservationsByStatus(ReservationStatus status){
        return reservationRepo.findByStatusOrderByReservationDateAsc(status);
    }

    public List<Reservation> getReservationsBetween(LocalDate start, LocalDate end){
        return reservationRepo.findByReservationDateBetweenOrderByReservationDateAsc(start, end);
    }

    public Reservation getReservationById(UUID id){
        return reservationRepo.findById(id).orElse(null);
    }

    public String updateReservation(UUID id, Reservation updated, UUID guestId, UUID waiterId){
        Optional<Reservation> getOne = reservationRepo.findById(id);
        if(getOne.isEmpty()){
            return NOT_FOUND;
        }
        if(missingFields(updated)){
            return FIELDS_REQUIRED;
        }
        Guest guest = null;
        if(guestId != null){
            Optional<Guest> getGuest = guestRepo.findById(guestId);
            if(getGuest.isEmpty()){
                return GuestService.NOT_FOUND;
            }
            guest = getGuest.get();
        }
        Waiter waiter = null;
        if(waiterId != null){
            Optional<Waiter> getWaiter = waiterRepo.findById(waiterId);
            if(getWaiter.isEmpty()){
                return WaiterService.NOT_FOUND;
            }
            waiter = getWaiter.get();
        }
        Reservation existing = getOne.get();
        existing.setReservationDate(updated.getReservationDate());
        existing.setPartySize(updated.getPartySize());
        existing.setTimeSlot(updated.getTimeSlot());
        if(updated.getStatus() != null){
            existing.setStatus(updated.getStatus());
        }
        if(guest != null){
            existing.setGuest(guest);
        }
        if(waiter != null){
            existing.setWaiter(waiter);
        }
        reservationRepo.save(existing);
        return UPDATED;
    }

    public String deleteReservation(UUID id){
        if(!reservationRepo.existsById(id)){
            return NOT_FOUND;
        }
        reservationRepo.deleteById(id);
        return DELETED;
    }

    //D2: satus flow. REQUESTED -> ACCEPTED or WITHDRAWN, accepted -> seated or withdrawn
    //Seated and withdrawn are final
    public String changeStatus(UUID id, ReservationStatus newStatus){
        Optional<Reservation> getOne = reservationRepo.findById(id);
        if(getOne.isEmpty()){
            return NOT_FOUND;
        }
        Reservation reservation = getOne.get();
        ReservationStatus current = reservation.getStatus();
        boolean allowed = (current == ReservationStatus.REQUESTED && (newStatus == ReservationStatus.ACCEPTED || newStatus == ReservationStatus.WITHDRAWN)) || (current == ReservationStatus.ACCEPTED && (newStatus == ReservationStatus.SEATED || newStatus == ReservationStatus.WITHDRAWN));
        if(!allowed){
            return CANNOT_CHANGE_PREFIX + current + " to " + newStatus;
        }
        reservation.setStatus(newStatus);
        reservationRepo.save(reservation);
        return STATUS_UPDATED;
    }

}