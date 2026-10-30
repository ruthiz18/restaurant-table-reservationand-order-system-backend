package kigali.restaurant.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.restaurant.rw.domain.Reservation;
import kigali.restaurant.rw.domain.ReservationStatus;
import kigali.restaurant.rw.domain.TableOrder;
import kigali.restaurant.rw.repository.ReservationRepository;
import kigali.restaurant.rw.repository.TableOrderRepository;

@Service
public class TableOrderService {

    public static final String SAVED = "Table order saved successfully";
    public static final String DELETED = "Table order deleted successfully";
    public static final String NOT_FOUND = "No table order exists with that id";
    public static final String NOT_SEATED = "Orders can only be added once the guests are seated";
    public static final String FIELDS_REQUIRED = "Please give a dish name and a quantity of at least 1";
    public static final String RESERVATION_ID_REQUIRED = "Please say which reservation this order belongs to";

    @Autowired
    private TableOrderRepository tableOrderRepo;

    @Autowired
    private ReservationRepository reservationRepo;

    // E1
    public String saveTableOrder(TableOrder tableOrder, UUID reservationId){
        if(tableOrder.getDishName() == null || tableOrder.getDishName().isBlank() || tableOrder.getQuantity() <= 0){
            return FIELDS_REQUIRED;
        }
        if(reservationId == null){
            return RESERVATION_ID_REQUIRED;
        }
        Optional<Reservation> found  =  reservationRepo.findById(reservationId);
        if(found.isEmpty()){
            return ReservationService.NOT_FOUND;
        }
        Reservation reservation  =  found.get();
        // orders only after seating
        if(reservation.getStatus() != ReservationStatus.SEATED){
            return NOT_SEATED;
        }
        tableOrder.setOrderDate(LocalDate.now());
        tableOrder.setReservation(reservation);
        tableOrderRepo.save(tableOrder);
        return SAVED;
    }

    public List<TableOrder> getAllTableOrders(){
        return tableOrderRepo.findAll();
    }

    public TableOrder getTableOrderById(UUID id){
        return tableOrderRepo.findById(id).orElse(null);
    }

    // E2
    public List<TableOrder> getTableOrdersOfGuest(String phone){
        return tableOrderRepo.findByGuestPhone(phone.trim());
    }

    // E3
    public String deleteTableOrder(UUID id){
        if(!tableOrderRepo.existsById(id)){
            return NOT_FOUND;
        }
        tableOrderRepo.deleteById(id);
        return DELETED;
    }
}
