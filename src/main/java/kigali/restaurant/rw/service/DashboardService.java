package kigali.restaurant.rw.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.restaurant.rw.domain.ReservationStatus;
import kigali.restaurant.rw.repository.DiningTableRepository;
import kigali.restaurant.rw.repository.GuestRepository;
import kigali.restaurant.rw.repository.ReservationRepository;
import kigali.restaurant.rw.repository.TableOrderRepository;
import kigali.restaurant.rw.repository.WaiterRepository;

@Service
public class DashboardService {

    @Autowired
    private GuestRepository guestRepo;

    @Autowired
    private WaiterRepository waiterRepo;

    @Autowired
    private DiningTableRepository diningTableRepo;

    @Autowired
    private ReservationRepository reservationRepo;

    @Autowired
    private TableOrderRepository tableOrderRepo;

    public Map<String,  Long> getSummary(){
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("guests", guestRepo.count());
        summary.put("waiters", waiterRepo.count());
        // C2
        summary.put("freeDiningTables", (long) diningTableRepo.findFreeDiningTables().size());
        summary.put("reservationsToday", reservationRepo.countByReservationDate(LocalDate.now()));
        summary.put("requested", reservationRepo.countByStatusEquals(ReservationStatus.REQUESTED));
        summary.put("tableOrders", tableOrderRepo.count());
        return summary;
    }
}
