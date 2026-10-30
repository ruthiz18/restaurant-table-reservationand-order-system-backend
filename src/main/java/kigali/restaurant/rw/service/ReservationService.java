package kigali.restaurant.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kigali.restaurant.rw.domain.Guest;
import kigali.restaurant.rw.domain.Reservation;
import kigali.restaurant.rw.domain.ReservationStatus;
import kigali.restaurant.rw.domain.Waiter;
import kigali.restaurant.rw.repository.GuestRepository;
import kigali.restaurant.rw.repository.ReservationRepository;
import kigali.restaurant.rw.repository.TableOrderRepository;
import kigali.restaurant.rw.repository.WaiterRepository;

@Service
public class ReservationService {

    public static final String SAVED = "Reservation saved successfully";
    public static final String UPDATED = "Reservation updated successfully";
    public static final String DELETED = "Reservation deleted successfully";
    public static final String STATUS_UPDATED = "Reservation status updated successfully";
    public static final String NOT_FOUND = "No reservation exists with that id";
    public static final String HAS_TABLE_ORDERS = "This reservation still has table orders, please delete them first";
    public static final String FIELDS_REQUIRED = "Please give a reservation date, a time slot and a party size of at least 1";
    public static final String GUEST_ID_REQUIRED = "Please say which guest is booking";
    public static final String WAITER_ID_REQUIRED = "Please say which waiter will serve the table";
    public static final String PAST_DATE = "The reservation date cannot be in the past";
    public static final String WAITER_BOOKED = "That waiter is already booked on that date";
    public static final String CANNOT_CHANGE_PREFIX = "Cannot change status from ";
    public static final String INVALID_DATE = "Start and end must be dates written as yyyy-MM-dd";
    public static final String INVALID_DAY = "The date must be written as yyyy-MM-dd";
    public static final String START_AFTER_END = "The start date cannot be after the end date";
    public static final String INVALID_PAGE = "Page must be 0 or more and size must be 1 or more";
    public static final String INVALID_SORT = "Sort must look like field,asc or field,desc, using reservationDate, status, partySize or timeSlot";
    public static final List<String> SORT_FIELDS = List.of("reservationDate", "status", "partySize", "timeSlot");

    @Autowired
    private ReservationRepository reservationRepo;

    @Autowired
    private TableOrderRepository tableOrderRepo;

    @Autowired
    private GuestRepository guestRepo;

    @Autowired
    private WaiterRepository waiterRepo;

    private boolean missingFields(Reservation reservation){
        return reservation.getReservationDate() == null
            || reservation.getTimeSlot() == null
            || reservation.getTimeSlot().isBlank()
            || reservation.getPartySize() <= 0;
    }

    public String saveReservation(Reservation reservation,  UUID guestId,  UUID waiterId){
        if(missingFields(reservation)){
            return FIELDS_REQUIRED;
        }
        if(reservation.getReservationDate().isBefore(LocalDate.now())){
            return PAST_DATE;
        }
        if(guestId == null){
            return GUEST_ID_REQUIRED;
        }
        if(waiterId == null){
            return WAITER_ID_REQUIRED;
        }
        Optional<Guest> guest  =  guestRepo.findById(guestId);
        if(guest.isEmpty()){
            return GuestService.NOT_FOUND;
        }
        Optional<Waiter> waiter  =  waiterRepo.findById(waiterId);
        if(waiter.isEmpty()){
            return WaiterService.NOT_FOUND;
        }
        // D1
        // waiter free that day
        if(reservationRepo.existsByWaiterIdAndReservationDateAndStatusNot(
                waiterId, reservation.getReservationDate(), ReservationStatus.WITHDRAWN)){
            return WAITER_BOOKED;
        }
        reservation.setGuest(guest.get());
        reservation.setWaiter(waiter.get());
        reservation.setStatus(ReservationStatus.REQUESTED);
        reservationRepo.save(reservation);
        return SAVED;
    }

    public List<Reservation> getAllReservations(){
        return reservationRepo.findAll();
    }

    // B2
    public List<Reservation> getReservationsByStatus(ReservationStatus status){
        return reservationRepo.findByStatusOrderByReservationDateAsc(status);
    }

    // B3
    public List<Reservation> getReservationsBetween(LocalDate start,  LocalDate end){
        return reservationRepo.findByReservationDateBetweenOrderByReservationDateAsc(start,  end);
    }

    public Reservation getReservationById(UUID id){
        return reservationRepo.findById(id).orElse(null);
    }

    public String updateReservation(UUID id, Reservation updated, UUID guestId, UUID waiterId){
        Optional<Reservation> found = reservationRepo.findById(id);
        if(found.isEmpty()){
            return NOT_FOUND;
        }
        if(missingFields(updated)){
            return FIELDS_REQUIRED;
        }
        Guest newGuest = null;
        if(guestId != null){
            Optional<Guest> guest = guestRepo.findById(guestId);
            if(guest.isEmpty()){
                return GuestService.NOT_FOUND;
            }
            newGuest  =  guest.get();
        }
        Waiter newWaiter  =  null;
        if(waiterId != null){
            Optional<Waiter> waiter = waiterRepo.findById(waiterId);
            if(waiter.isEmpty()){
                return WaiterService.NOT_FOUND;
            }
            newWaiter = waiter.get();
        }
        Reservation reservation  =  found.get();
        reservation.setReservationDate(updated.getReservationDate());
        reservation.setPartySize(updated.getPartySize());
        reservation.setTimeSlot(updated.getTimeSlot());
        if(newGuest != null){
            reservation.setGuest(newGuest);
        }
        if(newWaiter != null){
            reservation.setWaiter(newWaiter);
        }
        reservationRepo.save(reservation);
        return UPDATED;
    }

    // E3
    public String deleteReservation(UUID id){
        Optional<Reservation> found  =  reservationRepo.findById(id);
        if(found.isEmpty()){
            return NOT_FOUND;
        }
        if(tableOrderRepo.existsByReservation(found.get())){
            return HAS_TABLE_ORDERS;
        }
        // only when it has no orders
        reservationRepo.deleteById(id);
        return DELETED;
    }

    // D2
    public String changeStatus(UUID id, ReservationStatus newStatus){
        Optional<Reservation> found = reservationRepo.findById(id);
        if(found.isEmpty()){
            return NOT_FOUND;
        }
        Reservation reservation = found.get();
        ReservationStatus current  =  reservation.getStatus();
        // allowed status moves
        boolean fromRequested  =  current == ReservationStatus.REQUESTED
            && (newStatus == ReservationStatus.ACCEPTED || newStatus == ReservationStatus.WITHDRAWN);
        boolean fromAccepted = current == ReservationStatus.ACCEPTED
            && (newStatus == ReservationStatus.SEATED || newStatus == ReservationStatus.WITHDRAWN);
        if(!fromRequested && !fromAccepted){
            return CANNOT_CHANGE_PREFIX + current + " to " + newStatus;
        }
        reservation.setStatus(newStatus);
        reservationRepo.save(reservation);
        return STATUS_UPDATED;
    }

    // F1
    public List<Object[]> getCountByStatus(){
        return reservationRepo.countByStatus();
    }

    // F2
    public String cancelDay(UUID waiterId,  LocalDate date){
        // null means unknown waiter
        if(!waiterRepo.existsById(waiterId)){
            return null;
        }
        int cancelled = reservationRepo.cancelDay(waiterId, date,
            ReservationStatus.SEATED, ReservationStatus.WITHDRAWN);
        return cancelled + " reservations cancelled";
    }

    public Page<Reservation> getReservationsPage(Pageable pageable){
        return reservationRepo.findAll(pageable);
    }
}
