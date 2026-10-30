package kigali.restaurant.rw.domain;

import java.util.UUID;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "table_order")
public class TableOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "dish_name")
    private String dishName;

    @Column(name = "special_instructions")
    private String specialInstructions;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public UUID getId(){
        return id;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public String getDishName(){
        return dishName;
    }

    public void setDishName(String dishName){
        this.dishName = dishName;
    }

    public String getSpecialInstructions(){
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions){
        this.specialInstructions = specialInstructions;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity  =  quantity;
    }

    public LocalDate getOrderDate(){
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate){
        this.orderDate  =  orderDate;
    }

    public Reservation getReservation(){
        return reservation;
    }

    public void setReservation(Reservation reservation){
        this.reservation  =  reservation;
    }
}
