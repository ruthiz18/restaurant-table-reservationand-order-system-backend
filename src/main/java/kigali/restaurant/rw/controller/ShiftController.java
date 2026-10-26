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
import org.springframework.web.bind.annotation.RestController;

import kigali.restaurant.rw.domain.Shift;
import kigali.restaurant.rw.service.ShiftService;

@RestController 
@RequestMapping("/api/shifts")
public class ShiftController {
    
    @Autowired
    private ShiftService shiftService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveShift(@RequestBody Shift shift){
        String returnedMessage = shiftService.saveShift(shift);
        if(returnedMessage.equals(ShiftService.NAME_REQUIRED)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllShifts(){
        List<Shift> shifts = shiftService.getAllShift();
        return new ResponseEntity<>(shifts, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getShiftById(@PathVariable UUID id){
        Shift shift = shiftService.getShiftById(id);
        if(shift == null){
            return new ResponseEntity<>(ShiftService.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shift, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateShift(@PathVariable UUID id, @RequestBody Shift shift){
        String returnedMessage = shiftService.updateShift(id, shift);
        if(returnedMessage.equals(ShiftService.NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
        if(returnedMessage.equals(ShiftService.NAME_REQUIRED)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteShift(@PathVariable UUID id){
        String returnedMessage = shiftService.deleteShift(id);
        if(returnedMessage.equals(shiftService.NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
        if(returnedMessage.equals(ShiftService.IN_USE)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

}
