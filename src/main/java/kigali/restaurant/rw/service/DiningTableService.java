package kigali.restaurant.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.restaurant.rw.domain.DiningTable;
import kigali.restaurant.rw.repository.DiningTableRepository;
import kigali.restaurant.rw.repository.WaiterRepository;

@Service 
public class DiningTableService {

    public static final String SAVED = "Dining table saved successfully";
    public static final String UPDATED = "Dining table updated successfully";
    public static final String DELETED = "Dining table deleted successfully";
    public static final String NOT_FOUND = "Dining table not found";
    public static final String NUMBER_REQUIRED = "Dining table number is required";
    public static final String IN_USE = "The dining table is assigned to a waiter, remove it from the waiter first";

    @Autowired
    private DiningTableRepository diningTableRepo;

    @Autowired 
    private WaiterRepository waiterRepo;

    public String saveDiningTable(DiningTable diningTable){
        if(diningTable.getTableNumber() <= 0){
            return NUMBER_REQUIRED;
        }
        diningTableRepo.save(diningTable);
        return SAVED;
    }

    public List<DiningTable> getAllDiningTables(){
        return diningTableRepo.findAll();
    }
    
    public DiningTable getDiningTableById(UUID id){
        return diningTableRepo.findById(id).orElse(null);
    }

    public String updateDiningTable(UUID id, DiningTable updatedTable){
        Optional<DiningTable> getOne = diningTableRepo.findById(id);
        if(getOne.isEmpty()){
            return NOT_FOUND;
        }
        if(updatedTable.getTableNumber() <= 0) {
            return NUMBER_REQUIRED;
        }
        DiningTable existing = getOne.get();
        existing.setTableNumber(updatedTable.getTableNumber());
        existing.setName(updatedTable.getName());
        diningTableRepo.save(existing);
        return UPDATED;
    }

    public String deleteDiningTable(UUID id){
        if(!diningTableRepo.existsById(id)){
            return NOT_FOUND;
        }
        if(waiterRepo.existsByDiningTableId(id)){
            return IN_USE;
        }
        diningTableRepo.deleteById(id);
        return DELETED;
    }
    
}
