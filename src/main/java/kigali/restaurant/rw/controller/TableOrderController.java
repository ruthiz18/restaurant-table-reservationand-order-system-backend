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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.restaurant.rw.domain.TableOrder;
import kigali.restaurant.rw.service.TableOrderService;
import kigali.restaurant.rw.service.ReservationService;

@RestController
@RequestMapping("/api/table-orders")
public class TableOrderController {

    @Autowired
    private TableOrderService tableOrderService;

    // E1
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveTableOrder(@RequestBody TableOrder tableOrder,
            @RequestParam(required = false) UUID reservationId){
        String returnedMessage  =  tableOrderService.saveTableOrder(tableOrder,  reservationId);
        if(returnedMessage.equals(TableOrderService.SAVED)){
            return new ResponseEntity<>(returnedMessage,  HttpStatus.CREATED);
        }
        if(returnedMessage.equals(ReservationService.NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
    }

    // E2
    @GetMapping(value = "/guest/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTableOrdersOfGuest(@PathVariable String phone){
        List<TableOrder> orders  =  tableOrderService.getTableOrdersOfGuest(phone);
        return new ResponseEntity<>(orders,  HttpStatus.OK);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTableOrders(){
        List<TableOrder> orders = tableOrderService.getAllTableOrders();
        return new ResponseEntity<>(orders,  HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTableOrderById(@PathVariable UUID id){
        TableOrder order = tableOrderService.getTableOrderById(id);
        if(order == null){
            return new ResponseEntity<>(TableOrderService.NOT_FOUND,  HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    // E3
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteTableOrder(@PathVariable UUID id){
        String returnedMessage = tableOrderService.deleteTableOrder(id);
        if(returnedMessage.equals(TableOrderService.NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage,  HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }
}