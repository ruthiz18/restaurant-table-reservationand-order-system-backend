package kigali.restaurant.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.restaurant.rw.domain.Shift;
import kigali.restaurant.rw.repository.ShiftRepository;

@Service
public class ShiftService{
    public static final String SAVED = "Shift saved successfully";
    public static final String UPDATED = "Shift updated successfully";
    public static final String DELETED = "Shift deleted successfully";
    public static final String NOT_FOUND = "No shift exists with that id";
    public static final String NAME_REQUIRED = "Shift name is required";
    public static final String IN_USE = "Some waiters still hold this shift, please remove it from them first";

    @Autowired
    private ShiftRepository shiftRepo;

    public String saveShift(Shift shift){
        if(shift.getName() == null || shift.getName().isBlank()){
            return NAME_REQUIRED;
        }
        shift.setName(shift.getName().trim());
        shiftRepo.save(shift);
        return SAVED;
    }

    public List<Shift> getAllShift(){
        return shiftRepo.findAll();
    }

    public Shift getShiftById(UUID id){
        return shiftRepo.findById(id).orElse(null);
    }

    public String updateShift(UUID id, Shift updatedShift){
        Optional<Shift> getOne = shiftRepo.findById(id);
        if(getOne.isEmpty()){
            return NOT_FOUND;
        }
        if(updatedShift.getName() == null || updatedShift.getName().isBlank()){
            return NAME_REQUIRED;
        }
        Shift existing  =  getOne.get();
        existing.setName(updatedShift.getName().trim());
        shiftRepo.save(existing);
        return UPDATED;
    }

    @Transactional
    public String deleteShift(UUID id){
        Optional<Shift> getOne = shiftRepo.findById(id);
        if(getOne.isEmpty()){
            return NOT_FOUND;
        }
        if(!getOne.get().getWaiters().isEmpty()){
            return IN_USE;
        }
        shiftRepo.deleteById(id);
        return DELETED;
    }

    // C3
    public List<Shift> getUnusedShifts(){
        return shiftRepo.findUnusedShifts();
    }
}