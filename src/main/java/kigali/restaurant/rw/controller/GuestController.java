package kigali.restaurant.rw.controller;

import java.util.List;
import java.util.UUID;

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
  
import kigali.restaurant.rw.domain.Guest;
import kigali.restaurant.rw.service.GuestService;
import kigali.restaurant.rw.service.WaiterService;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    @Autowired
    private GuestService guestService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveGuest(@RequestBody Guest guest){
        String returnedMessage  =  guestService.saveGuest(guest);
        if (returnedMessage.equals(GuestService.FIELDS_REQUIRED)) {
            return new ResponseEntity<>(returnedMessage,  HttpStatus.BAD_REQUEST);
        }
        if(returnedMessage.equals(GuestService.PHONE_TAKEN)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(returnedMessage,  HttpStatus.CREATED);
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllGuests() {
        List<Guest> guests  =  guestService.getAllGuest();  
        return new ResponseEntity<>(guests,  HttpStatus.OK);
    }


    @GetMapping(value = "/by-last-name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGuestsByLastName(@RequestParam String lastName){
        List<Guest> guests  =  guestService.getGuestsByLastName(lastName);
        return new ResponseEntity<>(guests,  HttpStatus.OK);
    }

  
    // C4
    @GetMapping(value = "/of-waiter/{waiterId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGuestOfWaiter(@PathVariable UUID waiterId){  
        List<Guest> guests  =   guestService.getGuestsOfWaiter(waiterId);
        if (guests == null) {
            return new ResponseEntity<>(WaiterService.NOT_FOUND,  HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(guests,  HttpStatus.OK);
    }

  
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGuestById(@PathVariable UUID id){  
        Guest guest  =  guestService.getGuestById(id);
        if (guest == null) {
            return new ResponseEntity<>(GuestService.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(guest, HttpStatus.OK);
    }


    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateGuest(@PathVariable UUID id, @RequestBody Guest guest){
        String returnedMessage = guestService.updateGuest(id, guest);
        if(returnedMessage.equals(GuestService.NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage,  HttpStatus.NOT_FOUND);  
        }

        if(returnedMessage.equals(GuestService.FIELDS_REQUIRED)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }
        if (returnedMessage.equals(GuestService.PHONE_TAKEN)) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteGuest(@PathVariable UUID id){
        String returnedMessage  =   guestService.deleteGuest(id);
        if (returnedMessage.equals(GuestService.NOT_FOUND)) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND); 
        }  
        if(returnedMessage.equals(GuestService.IN_USE)){
            return new ResponseEntity<>(returnedMessage,  HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(returnedMessage,  HttpStatus.OK);
    }
}