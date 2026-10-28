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

import kigali.restaurant.rw.domain.DiningTable;
import kigali.restaurant.rw.service.DiningTableService;

@RestController 
@RequestMapping ("/api/dining-tables")
public class DiningTableController {

    @Autowired 
    private DiningTableService diningTableService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveDiningTable(@RequestBody DiningTable diningTable){
        String returnedMessage = diningTableService.saveDiningTable(diningTable);
        if(returnedMessage.equals(DiningTableService.NUMBER_REQUIRED)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllDiningTables(){
        List<DiningTable> tables = diningTableService.getAllDiningTables();
        return new ResponseEntity<>(tables, HttpStatus.OK);
    }

    //C2: free dining tables
    @GetMapping(value = "/free", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFreeDiningTables(){
        List<DiningTable> tables = diningTableService.getFreeDiningTables();
        return new ResponseEntity<>(tables, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDiningTableById(@PathVariable UUID id){
        DiningTable table = diningTableService.getDiningTableById(id);
        if(table == null){
            return new ResponseEntity<>(DiningTableService.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(table, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDiningTable(@PathVariable UUID id, @RequestBody DiningTable diningTable){
        String returnedMessage = diningTableService.updateDiningTable(id, diningTable);
        if(returnedMessage.equals(DiningTableService.NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
        if(returnedMessage.equals(DiningTableService.NUMBER_REQUIRED)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @DeleteMapping (value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteDiningTable(@PathVariable UUID id){
        String returnedMessage = diningTableService.deleteDiningTable(id);
        if(returnedMessage.equals(DiningTableService.NOT_FOUND)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
        if(returnedMessage.equals(DiningTableService.IN_USE)){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }



}
