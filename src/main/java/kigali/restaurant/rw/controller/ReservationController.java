package kigali.restaurant.rw.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PatchMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.restaurant.rw.domain.Reservation;
import kigali.restaurant.rw.domain.ReservationStatus;
import kigali.restaurant.rw.service.GuestService;
import kigali.restaurant.rw.service.ReservationService;
import kigali.restaurant.rw.service.WaiterService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveReservation(@RequestBody Reservation reservation,
            @RequestParam(required = false) UUID guestId,
            @RequestParam(required = false) UUID waiterId){
        String returnedMessage = reservationService.saveReservation(reservation, guestId, waiterId);
        if(returnedMessage.equals(ReservationService.SAVED)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllReservations(){
        List<Reservation> reservations = reservationService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping(value = "/by-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getReservationsByStatus(@RequestParam ReservationStatus status){
        List<Reservation> reservations = reservationService.getReservationsByStatus(status);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping(value = "/between", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getReservationsBetween(@RequestParam String start, @RequestParam String end){
        LocalDate startDate;
        LocalDate endDate;
        try{
            startDate = LocalDate.parse(start.trim());
            endDate = LocalDate.parse(end.trim());
        }catch(DateTimeParseException e){
            return new ResponseEntity<>(ReservationService.INVALID_DATE, HttpStatus.BAD_REQUEST);
        }
        if(startDate.isAfter(endDate)){
            return new ResponseEntity<>(ReservationService.START_AFTER_END, HttpStatus.BAD_REQUEST);
        }
        List<Reservation> reservations = reservationService.getReservationsBetween(startDate, endDate);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    //D2: status flow
    @PatchMapping(value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeStatus(@PathVariable UUID id, @RequestParam ReservationStatus status){
        String returnedMessage = reservationService.changeStatus(id, status);
        if(returnedMessage.equals(ReservationService.NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
        if(returnedMessage.startsWith(ReservationService.CANNOT_CHANGE_PREFIX)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getReservationById(@PathVariable UUID id){
        Reservation reservation = reservationService.getReservationById(id);
        if(reservation == null){
            return new ResponseEntity<>(ReservationService.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateReservation(@PathVariable UUID id, @RequestBody Reservation reservation,
            @RequestParam(required = false) UUID guestId,
            @RequestParam(required = false) UUID waiterId){
        String returnedMessage = reservationService.updateReservation(id, reservation, guestId, waiterId);
        if(returnedMessage.equals(ReservationService.NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
        if(returnedMessage.equals(ReservationService.FIELDS_REQUIRED)
            || returnedMessage.equals(GuestService.NOT_FOUND)
            || returnedMessage.equals(WaiterService.NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteReservation(@PathVariable UUID id){
        String returnedMessage = reservationService.deleteReservation(id);
        if(returnedMessage.equals(ReservationService.NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }
}