package kigali.restaurant.rw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.restaurant.rw.domain.DiningTable;
import kigali.restaurant.rw.domain.Shift;
import kigali.restaurant.rw.domain.Waiter;
import kigali.restaurant.rw.repository.DiningTableRepository;
import kigali.restaurant.rw.repository.ReservationRepository;
import kigali.restaurant.rw.repository.ShiftRepository;
import kigali.restaurant.rw.repository.WaiterRepository;

@Service
public class WaiterService {

    public static final String SAVED = "Waiter saved successfully";
    public static final String UPDATED = "Waiter updated successfully";
    public static final String DELETED = "Waiter deleted successfully";
    public static final String NOT_FOUND = "The waiter with that id does not exist";
    public static final String FIELDS_REQUIRED = "first name and last name are required";
    public static final String SHIFT_NOT_FOUND = "That shift does not exist";
    public static final String ALREADY_HAS_SHIFT = "The waiter already has that shift";
    public static final String SHIFT_ADDED = "Shift added to the waiter";
    public static final String TABLE_NUMBER_NOT_FOUND = "The dining table with that tableNumber does not exist";
    public static final String TABLE_TAKEN = "That dining table is taken by another waiter";
    public static final String TABLE_CHANGED = "Dining table changed successfully";
    public static final String IN_USE = "The waiter has reservations, delete them first";

    @Autowired
    private WaiterRepository waiterRepo;

    @Autowired
    private DiningTableRepository diningTableRepo;

    @Autowired
    private ShiftRepository shiftRepo;

    @Autowired
    private ReservationRepository reservationRepo;

    @Transactional
    public String saveWaiter(Waiter waiter, UUID diningTableId, List<UUID> shiftIds){
        if(waiter.getFirstName() == null || waiter.getFirstName().isBlank()
            || waiter.getLastName() == null || waiter.getLastName().isBlank()){
            return FIELDS_REQUIRED;
        }
        DiningTable diningTable = null;
        if(diningTableId != null){
            Optional<DiningTable> getTable = diningTableRepo.findById(diningTableId);
            if(getTable.isEmpty()){
                return DiningTableService.NOT_FOUND;
            }
            diningTable = getTable.get();
        }
        List<Shift> shifts = new ArrayList<>();
        String problem = findShifts(shiftIds, shifts);
        if(problem != null){
            return problem;
        }
        waiter.setDiningTable(diningTable);
        waiter.setShifts(shifts);
        waiterRepo.save(waiter);
        return SAVED;
    }

    public List<Waiter> getAllWaiters(){
        return waiterRepo.findAll();
    }

    public Waiter getWaiterById(UUID id){
        return waiterRepo.findById(id).orElse(null);
    }

    @Transactional
    public String updateWaiter(UUID id, Waiter updatedWaiter, UUID diningTableId, List<UUID> shiftIds){
        Optional<Waiter> getOne = waiterRepo.findById(id);
        if(getOne.isEmpty()){
            return NOT_FOUND;
        }
        if(updatedWaiter.getFirstName() == null || updatedWaiter.getFirstName().isBlank()
            || updatedWaiter.getLastName() == null || updatedWaiter.getLastName().isBlank()){
            return FIELDS_REQUIRED;
        }
        DiningTable diningTable = null;
        if(diningTableId != null){
            Optional<DiningTable> getTable = diningTableRepo.findById(diningTableId);
            if(getTable.isEmpty()){
                return DiningTableService.NOT_FOUND;
            }
            diningTable = getTable.get();
        }
        List<Shift> shifts = null;
        if(shiftIds != null){
            shifts = new ArrayList<>();
            String problem = findShifts(shiftIds, shifts);
            if(problem != null){
                return problem;
            }
        }
        Waiter existing = getOne.get();
        existing.setFirstName(updatedWaiter.getFirstName());
        existing.setLastName(updatedWaiter.getLastName());
        existing.setPhone(updatedWaiter.getPhone());
        if(diningTable != null){
            existing.setDiningTable(diningTable);
        }
        if(shifts != null){
            existing.setShifts(shifts);
        }
        waiterRepo.save(existing);
        return UPDATED;
    }

    public String deleteWaiter(UUID id){
        if(!waiterRepo.existsById(id)){
            return NOT_FOUND;
        }
        if(reservationRepo.existsByWaiterId(id)){
            return IN_USE;
        }
        waiterRepo.deleteById(id);
        return DELETED;
    }

    private String findShifts(List<UUID> ids, List<Shift> found){
        if(ids == null){
            return null;
        }
        for(UUID shiftId : ids){
            Optional<Shift> one = shiftRepo.findById(shiftId);
            if(one.isEmpty()){
                return ShiftService.NOT_FOUND;
            }
            if(!found.contains(one.get())){
                found.add(one.get());
            }
        }
        return null;
    }

    //C1: waiters by shift name
    public List<Waiter> getWaitersByShift(String name){
        return waiterRepo.findByShiftName(name.trim());
    }

    //D3: add on shift, keep the ones already there
    @Transactional 
    public String addShift(UUID waiterId, UUID shiftId){
        Optional<Waiter> getWaiter = waiterRepo.findById(waiterId);
        if(getWaiter.isEmpty()){
            return NOT_FOUND;
        }
        Optional<Shift> getShift = shiftRepo.findById(shiftId);
        if(getShift.isEmpty()){
            return SHIFT_NOT_FOUND;
        }
        Waiter waiter = getWaiter.get();
        boolean alreadyHas = waiter.getShifts().stream().anyMatch(sh -> sh.getId().equals(shiftId));
        if(alreadyHas){
            return ALREADY_HAS_SHIFT;
        }
        waiter.getShifts().add(getShift.get());;
        waiterRepo.save(waiter);
        return SHIFT_ADDED;
    }

    //D4: move a waiter to another dining table, the old one becomes free
    @Transactional 
    public String changeDiningTable(UUID waiterId, int tableNumber){
        Optional<Waiter> getWaiter = waiterRepo.findById(waiterId);
        if(getWaiter.isEmpty()){
            return NOT_FOUND;
        }
        Optional<DiningTable> getTable = diningTableRepo.findByTableNumber(tableNumber);
        if(getTable.isEmpty()){
            return TABLE_NUMBER_NOT_FOUND;
        }
        if(waiterRepo.existsByDiningTableTableNumberAndIdNot(tableNumber, waiterId)){
            return TABLE_TAKEN;
        }
        Waiter waiter = getWaiter.get();
        waiter.setDiningTable(getTable.get());
        waiterRepo.save(waiter);
        return TABLE_CHANGED;
    }

}