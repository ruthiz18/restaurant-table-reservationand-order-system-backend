package kigali.restaurant.rw.controller;

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
 
import kigali.restaurant.rw.domain.Waiter;
import kigali.restaurant.rw.service.DiningTableService;
import kigali.restaurant.rw.service.ShiftService;
import kigali.restaurant.rw.service.WaiterService;

@RestController
@RequestMapping("/api/waiters")
public class WaiterController {

    @Autowired
    private WaiterService waiterService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveWaiter(@RequestBody Waiter waiter, @RequestParam(required = false) UUID diningTableId, @RequestParam(required = false) List<UUID> shiftIds){
        String returnedMessage = waiterService.saveWaiter(waiter, diningTableId, shiftIds);
        if(returnedMessage.equals(WaiterService.FIELDS_REQUIRED) || returnedMessage.equals(DiningTableService.NOT_FOUND) || returnedMessage.equals(ShiftService.NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllWaiters(){
        List<Waiter> waiters = waiterService.getAllWaiters();
        return new ResponseEntity<>(waiters, HttpStatus.OK);
    }

    //C1: waiters by shift
    @GetMapping(value = "/by-shift", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWaitersByShift(@RequestParam String name){
        if(name.isBlank()){
            return new ResponseEntity<>(ShiftService.NAME_REQUIRED, HttpStatus.BAD_REQUEST);
        }
        List<Waiter> waiters = waiterService.getWaitersByShift(name);
        return new ResponseEntity<>(waiters, HttpStatus.OK);
    }

    //D3: add one shift to a waiter
    @PutMapping(value = "/{waiterId}/shifts/{shiftId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addShift(@PathVariable UUID waiterId, @PathVariable UUID shiftId){
        String returnedMessage = waiterService.addShift(waiterId, shiftId);
        if(returnedMessage.equals(WaiterService.NOT_FOUND) || returnedMessage.equals(WaiterService.SHIFT_NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
        if(returnedMessage.equals(WaiterService.ALREADY_HAS_SHIFT)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    //D4: move a waiter to another dining table
    @PatchMapping(value = "/{waiterId}/dining-table", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeDiningTable(@PathVariable UUID waiterId, @RequestParam int tableNumber){
        String returnedMessage = waiterService.changeDiningTable(waiterId, tableNumber);
        if(returnedMessage.equals(WaiterService.NOT_FOUND) || returnedMessage.equals(WaiterService.TABLE_NUMBER_NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
        if(returnedMessage.equals(WaiterService.TABLE_TAKEN)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWaiterById(@PathVariable UUID id){
        Waiter waiter = waiterService.getWaiterById(id);
        if(waiter == null){
            return new ResponseEntity<>(WaiterService.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(waiter, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateWaiter(@PathVariable UUID id, @RequestBody Waiter waiter, @RequestParam(required = false) UUID diningTableId, @RequestParam(required = false) List<UUID> shiftIds){
        String returnedMessage = waiterService.updateWaiter(id, waiter, diningTableId, shiftIds);
        if(returnedMessage.equals(WaiterService.NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
        if(returnedMessage.equals(WaiterService.FIELDS_REQUIRED) || returnedMessage.equals(DiningTableService.NOT_FOUND) || returnedMessage.equals(ShiftService.NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteWaiter(@PathVariable UUID id){
        String returnedMessage = waiterService.deleteWaiter(id);
        if(returnedMessage.equals(WaiterService.NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
        if(returnedMessage.equals(WaiterService.IN_USE)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }
}