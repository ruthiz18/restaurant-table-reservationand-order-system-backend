package kigali.restaurant.rw.domain;

import java.time.LocalDate;
import java.util.UUID;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name="reservation")
public class Reservation {
    
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @Column (name="reservation_date")
    private LocalDate reservationDate;

    @Enumerated (EnumType.STRING)
    @Column (name="status")
    private ReservationStatus status;  

    @Column (name="party_size")
    private int partySize;

    @Column (name="time_slot")
    private String timeSlot;
  
    @ManyToOne
    @JoinColumn (name="guest_id")
    private Guest guest;

    @ManyToOne
    @JoinColumn (name="waiter_id")
    private Waiter waiter;

    public UUID getId(){
        return id;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate){
        this.reservationDate = reservationDate;
    }
  
    public ReservationStatus getStatus() {
        return status;
    }


    public void setStatus(ReservationStatus status){
        this.status  =   status;
    }  


    public int getPartySize(){
        return partySize;
    }


    public void setPartySize(int partySize) {
        this.partySize  =   partySize;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot){
        this.timeSlot = timeSlot;
    }

    public Guest getGuest(){
        return guest;
    }


    public void setGuest(Guest guest){  
        this.guest  =  guest;
    }
  
    public Waiter getWaiter(){
        return waiter;
    }  
  
    public void setWaiter(Waiter waiter){  
        this.waiter  =  waiter;
    }



}
